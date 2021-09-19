package com.marleyspoon.listing

import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marleyspoon.base_android.*
import com.marleyspoon.listing.repo.RecipeRepository
import com.marleyspoon.listing.repo.RecipeRepository.NetworkState
import com.marleyspoon.listing.repo.RecipeRepository.Page
import com.marleyspoon.listing.udf.Store
import com.marleyspoon.listing.ui.*
import com.marleyspoon.listing.ui.list.RecipeListingFragment
import com.marleyspoon.listing.ui.list.ViewModelFactoryProducer
import com.marleyspoon.listing.utils.RecyclerViewMatchers.hasItemCount
import com.marleyspoon.listing.utils.TestUtil
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(NavigatorModule::class)
@RunWith(AndroidJUnit4::class)
class RecipeListingFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val store by lazy { Store(UIState()) }

    private val vm: RecipesViewModel by lazy {
        RecipesViewModel(SavedStateHandle(), mockk(relaxed = true), store)
    }

    @Before
    fun setup() {

        launchFragmentInHiltContainer(
            themeResId = R.style.Theme_marleyspoon,
            instantiate = {
                ViewModelFactoryProducer.producer = {
                    ViewModelUtil.createFor(vm)
                }
                RecipeListingFragment()
            }
        ) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    // The fragment’s view has just been created
                    disableProgressBarAnimations()
                }
            }
        }
    }

    @Test
    fun shouldDisplayRecipes() {

        onView(withId(R.id.progress)).check(matches(isDisplayed()))
        onView(withId(R.id.list)).check(matches(not(isDisplayed())))
        onView(withId(R.id.errorViewContainer)).check(matches(not(isDisplayed())))

        store.setState {
            it.copy(
                page = Page(
                    state = NetworkState.SUCCESS,
                    data = MutableList(15) { TestUtil.createUIRecipe("$it") }
                )
            )
        }

        onView(withId(R.id.list)).check(matches(isDisplayed()))
        onView(withId(R.id.list)).check(matches(hasItemCount(15)))
        onView(withId(R.id.errorViewContainer)).check(matches(not(isDisplayed())))

    }
}