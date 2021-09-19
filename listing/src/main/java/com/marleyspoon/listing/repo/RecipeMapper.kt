package com.marleyspoon.listing.repo

import com.contentful.java.cda.CDAArray
import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.LocalizedResource
import com.marleyspoon.listing.ui.UIRecipe
import javax.inject.Inject

class RecipeMapper @Inject constructor() {
    operator fun invoke(response: CDAArray): List<UIRecipe> = response.items().map {
        (it as LocalizedResource).run {
            UIRecipe(
                id = it.id(),
                title = mapTitle(it),
                subtitle = mapSubtitle(it),
                photo = mapUrl(it),
                chef = mapChef(it),
                description = it.getField("description"),
                tags = mapTags(it)
            )
        }
    }

    private fun mapTitle(it: LocalizedResource): String {
        val title = it.getField<String>("title")
        val index = title.indexOf("with")
        return title.subSequence(0, index).toString()
    }

    private fun mapSubtitle(it: LocalizedResource): String {
        val title = it.getField<String>("title")
        val index = title.indexOf("with")
        return title.subSequence(index, title.length - 1).toString()
    }

    private fun mapTags(it: LocalizedResource): List<String> {
        return it.getField<List<CDAEntry>>("tags")?.map { entry -> entry.getField("name") }
            ?: listOf()
    }

    private fun mapUrl(it: LocalizedResource): String {
        val url = it.getField<CDAAsset>("photo")?.getField<Map<String, Any>>("file")
            ?.get("url") as? String ?: ""
        return "https:$url"
    }

    private fun mapChef(it: LocalizedResource): String? {
        val f = it.getField<CDAEntry>("chef") ?: return null
        return f.getField<String>("name")
    }
}