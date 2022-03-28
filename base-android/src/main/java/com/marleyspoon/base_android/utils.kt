package com.marleyspoon.base_android

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun View.visible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
