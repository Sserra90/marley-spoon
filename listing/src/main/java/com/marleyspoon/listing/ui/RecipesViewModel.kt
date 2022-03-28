package com.marleyspoon.listing.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.marleyspoon.base_android.AsyncValue
import com.marleyspoon.listing.repo.RecipeRepository
import com.marleyspoon.listing.udf.Store
import com.marleyspoon.listing.udf.StoreViewModel
import com.marleyspoon.listing.udf.UIAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repo: RecipeRepository,
    store: Store<UIState>
) : StoreViewModel<UIState>(store) {

    init {
        repo.feed.onEach { page ->
            setState { copy(page = page) }
        }.launchIn(viewModelScope)
    }

    override fun onUserAction(action: UIAction) {
        when (action) {
            is LoadRecipes -> viewModelScope.launch {
                repo.getRecipes()
            }
            is GetRecipe -> {
                suspend { repo.getRecipe(action.id) }.execute {
                    copy(recipe = it)
                }
            }
        }
    }

}