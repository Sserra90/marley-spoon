package com.marleyspoon.listing.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.StringRes
import com.marleyspoon.listing.R
import com.marleyspoon.listing.databinding.ErrorViewBinding
import com.marleyspoon.base_android.FailureType
import java.lang.IllegalStateException

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var binding = ErrorViewBinding.inflate(LayoutInflater.from(context), this)

    fun setMessage(@StringRes resId: Int) {
        binding.message.setText(resId)
    }

    fun setRetryText(@StringRes resId: Int) {
        binding.retryBtn.setText(resId)
    }

    fun setRetryListener(onRetry: () -> Unit = {}) {
        binding.retryBtn.setOnClickListener { onRetry() }
    }
}

class ErrorViewFactory(private val context: Context) {
    @Suppress("UNCHECKED_CAST")
    fun create(
        failureType: FailureType,
        onRetry: () -> Unit,
        builder: ((featureFailure: FailureType.FeatureFailure<*>) -> View)? = null
    ): View {
        return when (failureType) {
            is FailureType.FeatureFailure<*> -> {

                if (builder == null) {
                    throw IllegalStateException("Builder should not be null")
                }

                builder(failureType)
            }
            FailureType.NetworkFailure -> {
                ErrorView(context).apply {
                    setMessage(R.string.no_internet_label)
                    setRetryListener(onRetry)
                }
            }
            else -> ErrorView(context).apply {
                setRetryListener(onRetry)
            }
        }
    }
}