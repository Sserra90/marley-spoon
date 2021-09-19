package com.marleyspoon.listing.ui

import com.marleyspoon.base_android.AsyncValue
import com.marleyspoon.listing.udf.State
import com.marleyspoon.listing.udf.UIAction
import com.marleyspoon.listing.repo.RecipeRepository.*

data class UIState(
    val page: Page = Page(),
    val recipe: AsyncValue<UIRecipe> = AsyncValue.None
) : State()

object LoadRecipes : UIAction()
data class GetRecipe(val id: String) : UIAction()

