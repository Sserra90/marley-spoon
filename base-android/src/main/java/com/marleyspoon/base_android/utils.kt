package com.marleyspoon.base_android

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> LifecycleOwner.onDestroyNullable(): ReadWriteProperty<LifecycleOwner, T> =
    object : ReadWriteProperty<LifecycleOwner, T>, DefaultLifecycleObserver {

        private var value: T? = null

        init {
            this@onDestroyNullable
                .lifecycle
                .addObserver(this)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            value = null
            this@onDestroyNullable
                .lifecycle
                .removeObserver(this)
            super.onDestroy(owner)
        }

        override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
            this.value = value
        }

        override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
            return value!!
        }
    }

fun View.visible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
