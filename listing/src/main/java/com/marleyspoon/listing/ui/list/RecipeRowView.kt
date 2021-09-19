package com.marleyspoon.listing.ui.list

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.marginEnd
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.marleyspoon.base_android.visible
import com.marleyspoon.listing.R
import com.marleyspoon.listing.databinding.RecipeRowLayoutBinding


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class RecipeRowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    var binding: RecipeRowLayoutBinding =
        RecipeRowLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    @ModelProp
    fun setImage(url: String) {
        ViewCompat.setTransitionName(binding.image, url)
        Glide.with(this).load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.image)
    }

    @TextProp
    fun setTitle(name: CharSequence) {
        binding.title.text = name
    }

    @TextProp
    fun setSubtitle(name: CharSequence) {
        binding.subtitle.text = name
    }

    @TextProp
    fun setChefName(name: CharSequence) {
        binding.chefName.visible(name.isNotEmpty())
        binding.chefName.text = "by $name"
    }

    @ModelProp
    fun setTags(tags: List<String>) {
        binding.tags.visible(tags.isNotEmpty())
        binding.tags.removeAllViews()

        tags.forEach {
            val tag = LayoutInflater.from(context)
                .inflate(R.layout.tag_view, binding.tags, false) as TextView
            tag.text = it
            binding.tags.addView(tag)
        }
    }

    var clickListener: OnClickListener? = null
        @CallbackProp set(value) {
            binding.card.setOnClickListener(value)
        }

}