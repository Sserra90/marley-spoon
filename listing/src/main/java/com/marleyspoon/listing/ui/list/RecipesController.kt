package com.marleyspoon.listing.ui.list

import com.airbnb.epoxy.TypedEpoxyController
import com.marleyspoon.listing.ui.UIRecipe

class RecipesController(
    private val onItemClick: (recipeId: String) -> Unit
) : TypedEpoxyController<List<UIRecipe>>() {
    override fun buildModels(data: List<UIRecipe>?) {
        data?.forEach {
            RecipeRowViewModel_()
                .id(it.id)
                .image(it.photo)
                .subtitle(it.subtitle)
                .chefName(it.chef ?: "")
                .tags(it.tags)
                .title(it.title)
                .clickListener { _, _, _, _ ->
                    onItemClick(it.id)
                }
                .addTo(this)
        }
    }
}