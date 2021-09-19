package com.marleyspoon.listing.ui


data class UIRecipe(
    val id: String,
    val title: String,
    val subtitle: String,
    val photo:String,
    val description: String,
    val chef: String?,
    val tags: List<String>
)
