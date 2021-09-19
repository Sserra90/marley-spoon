package com.marleyspoon.base_android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Creates a one off view model factory for the given view model instance.
 * Reference: https://github.com/android/architecture-components-samples/blob/main/GithubBrowserSample/app/src/androidTest/java/com/android/example/github/util/ViewModelUtil.kt
 */
object ViewModelUtil {
    fun <T : ViewModel> createFor(model: T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(model.javaClass)) {
                    @Suppress("UNCHECKED_CAST")
                    return model as T
                }
                throw IllegalArgumentException("unexpected model class $modelClass")
            }
        }
    }
}