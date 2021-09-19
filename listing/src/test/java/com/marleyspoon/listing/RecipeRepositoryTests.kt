package com.marleyspoon.listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.contentful.java.cda.CDAArray
import com.contentful.java.cda.CDAClient
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.CDAResource
import com.marleyspoon.listing.repo.ContentfulRecipeRepository
import com.marleyspoon.listing.repo.RecipeMapper
import com.marleyspoon.listing.repo.RecipeRepository
import com.marleyspoon.listing.repo.RecipeRepository.NetworkState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RecipeRepositoryTests {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var client: CDAClient

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `it should emit loading and success`(): Unit = runBlockingTest {

        every {
            client.fetch(CDAEntry::class.java).withContentType("recipe")
                .all()
        } returns mockk(relaxed = true)

        val repo = ContentfulRecipeRepository(client, RecipeMapper(), testDispatcher)

        val items = mutableListOf<RecipeRepository.Page>()
        val job = launch {
            repo.feed.toList(items)
        }

        repo.getRecipes()

        // THEN
        assertEquals(2, items.size)
        assertEquals(NetworkState.LOADING, items[0].state)
        assertEquals(NetworkState.SUCCESS, items[1].state)
        job.cancel()
    }
}