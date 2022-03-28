package com.marleyspoon.listing.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.marleyspoon.base_android.FailureType
import com.marleyspoon.base_android.viewBinding
import com.marleyspoon.base_android.visible
import com.marleyspoon.listing.Navigator
import com.marleyspoon.listing.R
import com.marleyspoon.listing.databinding.RecipesListingFragmentBinding
import com.marleyspoon.listing.repo.RecipeRepository.NetworkState
import com.marleyspoon.listing.udf.Renderable
import com.marleyspoon.listing.udf.Store
import com.marleyspoon.listing.ui.LoadRecipes
import com.marleyspoon.listing.ui.RecipesViewModel
import com.marleyspoon.listing.ui.UIState
import com.marleyspoon.listing.ui.detail.HasToolbar
import com.marleyspoon.listing.ui.views.ErrorViewFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * It seems that with the new view model delegate it becomes very difficult
 * to inject a mocked view model. More info here:
 * https://proandroiddev.com/testing-the-untestable-the-case-of-the-viewmodel-delegate-975c09160993
 *
 * This is just a "hacky" workaround
 */
object ViewModelFactoryProducer {
    var producer: (() -> ViewModelProvider.Factory)? = null
}

@AndroidEntryPoint
class RecipeListingFragment : Fragment(R.layout.recipes_listing_fragment), Renderable<UIState> {

    @Inject
    lateinit var navigator: Navigator

    private val vm: RecipesViewModel by activityViewModels(ViewModelFactoryProducer.producer)
    private val binding by viewBinding(RecipesListingFragmentBinding::bind)

    private val controller = RecipesController(onItemClick = { id ->
        navigator.openRecipePage(id)
    })
    private val errorViewFactory by lazy { ErrorViewFactory(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            loadRecipes()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.setItemSpacingDp(16)
        binding.list.setController(controller)
        Store.Binder<UIState>().bind(this, vm)
    }

    private fun loadRecipes() {
        vm.onUserAction(LoadRecipes)
    }

    override fun render(state: UIState) {
        renderRecipes(state)
    }

    private fun renderRecipes(state: UIState) = with(binding) {
        when (state.page.state) {
            NetworkState.LOADING -> {
                progress.visible(true)
                list.visible(false)
                errorViewContainer.visible(false)
            }
            NetworkState.ERROR -> {
                progress.visible(false)
                with(errorViewContainer) {
                    visible(true)
                    removeAllViews()
                    addView(
                        errorViewFactory.create(
                            FailureType.NetworkFailure,
                            onRetry = { loadRecipes() })
                    )
                }
            }
            NetworkState.SUCCESS -> {
                progress.visible(false)
                errorViewContainer.visible(false)
                list.visible(true)
                controller.setData(state.page.data)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as? HasToolbar)?.run {
            restore()
        }
    }

}