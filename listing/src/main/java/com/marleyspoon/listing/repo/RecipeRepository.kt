package com.marleyspoon.listing.repo

import com.contentful.java.cda.CDAArray
import com.contentful.java.cda.CDAClient
import com.contentful.java.cda.CDAEntry
import com.marleyspoon.base_android.AsyncValue
import com.marleyspoon.listing.repo.RecipeRepository.NetworkState.*
import com.marleyspoon.listing.repo.RecipeRepository.Page
import com.marleyspoon.listing.ui.UIRecipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RecipeRepository {
    enum class NetworkState { LOADING, ERROR, SUCCESS }
    data class Page(
        val data: List<UIRecipe> = listOf(),
        val state: NetworkState = LOADING
    )

    val feed: StateFlow<Page>
    suspend fun getRecipes()
    suspend fun getRecipe(id: String): UIRecipe
}

class ContentfulRecipeRepository @Inject constructor(
    private val client: CDAClient,
    private val mapper: RecipeMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeRepository {

    private val lv: MutableStateFlow<Page> = MutableStateFlow(Page())
    override val feed: StateFlow<Page> = lv

    override suspend fun getRecipes() = withContext(dispatcher) {

        // Set loading
        emit {
            copy(state = LOADING)
        }

        try {
            emit {
                copy(state = SUCCESS, data = mapper(makeCall()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit { copy(state = ERROR) }
        }
    }

    private fun makeCall(): CDAArray {
        return client
            .fetch(CDAEntry::class.java)
            .withContentType("recipe")
            .all()
    }

    override suspend fun getRecipe(id: String): UIRecipe {
        val recipe = lv.value.data.find { it.id == id }

        // If it's not locally make a request to fetch it
        // getRecipe(id)

        return recipe!!
    }

    private fun emit(block: Page.() -> Page) {
        lv.value = lv.value.block()
    }
}