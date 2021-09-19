package com.marleyspoon.listing.utils

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.airbnb.epoxy.EpoxyRecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher

object RecyclerViewMatchers {
    @JvmStatic
    fun hasItemCount(itemCount: Int): Matcher<View> {
        return object : BoundedMatcher<View, EpoxyRecyclerView>(
            EpoxyRecyclerView::class.java
        ) {

            override fun describeTo(description: Description) {
                description.appendText("has $itemCount items")
            }

            override fun matchesSafely(view: EpoxyRecyclerView): Boolean {
                // Divider
                return view.adapter!!.itemCount == itemCount
            }
        }
    }
}