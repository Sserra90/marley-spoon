package com.marleyspoon.listing.utils

import com.marleyspoon.listing.ui.UIRecipe

object TestUtil {

    fun createUIRecipe(id: String): UIRecipe {
        return UIRecipe(
            id,
            "title",
            "subtitle",
            "aPhoto",
            "aDescription",
            "aChef",
            listOf()
        )
    }
}