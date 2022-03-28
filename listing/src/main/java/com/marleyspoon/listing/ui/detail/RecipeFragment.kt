package com.marleyspoon.listing.ui.detail

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.parseAsHtml
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.marleyspoon.base_android.*
import com.marleyspoon.listing.R
import com.marleyspoon.listing.databinding.RecipeFragmentBinding
import com.marleyspoon.listing.udf.Renderable
import com.marleyspoon.listing.udf.Store
import com.marleyspoon.listing.ui.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

interface HasToolbar {
    fun setTitle(title: String)
    fun restore()
    fun setColor(color: Int)
    fun setStatusBarColor(color: Int)
    fun restoreStatusBarColor()
}

@AndroidEntryPoint
class RecipeFragment : Fragment(R.layout.recipe_fragment), Renderable<UIState> {

    private val args by navArgs<RecipeFragmentArgs>()
    private val vm: RecipesViewModel by activityViewModels()
    private val binding by viewBinding(RecipeFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Store.Binder<UIState>().bind(this, vm)
        if (savedInstanceState == null) {
            vm.onUserAction(GetRecipe(args.id))
        }
    }

    override fun render(state: UIState) {
        renderRecipe(state.recipe)
    }

    private fun renderRecipe(state: AsyncValue<UIRecipe>) = with(binding) {
        when (state) {
            is AsyncValue.Error -> {
                // TODO show error
                progress.visible(false)
                content.visible(false)
            }
            AsyncValue.Loading -> {
                content.visible(false)
                progress.visible(true)
            }
            is AsyncValue.Success -> {
                content.visible(true)
                progress.visible(false)
                renderContent(state.data)
            }
            else -> {
            }
        }
    }

    private fun renderContent(recipe: UIRecipe) = with(binding) {
        ViewCompat.setTransitionName(binding.image, recipe.photo)
        loadImage(recipe.photo, binding.image)

        title.text = recipe.title

        chefName.visible(!recipe.chef.isNullOrEmpty())
        chefName.text = "by ${recipe.chef}"

        description.text = recipe.description.parseAsHtml()

        tags.visible(recipe.tags.isNotEmpty())
        tags.removeAllViews()

        recipe.tags.forEach {
            val tag = LayoutInflater.from(context)
                .inflate(R.layout.tag_view, binding.tags, false) as TextView
            tag.text = it
            tags.addView(tag)
        }

        (requireActivity() as HasToolbar).setTitle(recipe.title)
    }

    private fun loadImage(photo: String, view: ImageView) {

        Glide.with(requireContext())
            .asBitmap()
            .load(photo)
            .transition(BitmapTransitionOptions.withCrossFade())
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource != null) {
                        val p = Palette.from(resource).generate()
                        (requireActivity() as HasToolbar).run {
                            setColor(p.getLightVibrantColor(Color.WHITE))
                            setStatusBarColor(p.getLightVibrantColor(Color.WHITE))
                        }
                    }
                    return false
                }
            })
            .into(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as HasToolbar).run {
            setColor(Color.WHITE)
            restoreStatusBarColor()
        }
    }
}
