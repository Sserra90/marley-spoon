package com.marleyspoon.listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.SavedStateHandle
import com.marleyspoon.base_android.MainCoroutineRule
import com.marleyspoon.listing.repo.RecipeRepository
import com.marleyspoon.listing.repo.RecipeRepository.NetworkState
import com.marleyspoon.listing.udf.Renderable
import com.marleyspoon.listing.udf.Store
import com.marleyspoon.listing.ui.*
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RecipeViewModelTests {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @RelaxedMockK
    lateinit var repo: RecipeRepository

    private val store by lazy { Store(UIState()) }

    private val _lifecycle by lazy {
        LifecycleRegistry(mockk()).apply {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }

    private val view by lazy {
        mockk<Renderable<UIState>>(relaxed = true).apply {
            every { lifecycle } returns _lifecycle
            excludeRecords {
                lifecycle
            }
        }
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should load recipes and update state`() {

        val firstPage = RecipeRepository.Page(
            data = listOf(),
            state = NetworkState.LOADING
        )
        val feed = MutableStateFlow(firstPage)
        coEvery { repo.feed } returns feed

        val vm = RecipesViewModel(SavedStateHandle(), repo, store)
        Store.Binder<UIState>().bind(view, vm)

        val recipes = listOf(
            UIRecipe(
                "id",
                "title",
                "subtitle",
                "aPhoto",
                "aDescription",
                "aChef",
                listOf()
            )
        )
        val secondPage = RecipeRepository.Page(
            data = recipes,
            state = NetworkState.SUCCESS
        )

        // Update feed
        feed.value = secondPage

        verifySequence {
            view.render(UIState(page = firstPage))
            view.render(UIState(page = secondPage))
        }
    }

}