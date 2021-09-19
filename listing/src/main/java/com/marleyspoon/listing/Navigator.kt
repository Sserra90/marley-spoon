package com.marleyspoon.listing

import androidx.navigation.NavController
import com.marleyspoon.listing.ui.list.RecipeListingFragmentDirections

class Navigator(private val navController: NavController) {
    fun openRecipePage(id: String) {
        navController.navigate(RecipeListingFragmentDirections.toRecipe(id))
    }
}