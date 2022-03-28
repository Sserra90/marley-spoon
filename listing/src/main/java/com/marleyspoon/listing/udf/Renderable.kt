package com.marleyspoon.listing.udf

import androidx.lifecycle.LifecycleOwner


interface Renderable<T : State> : LifecycleOwner {
    fun render(state: T) {}
    fun runEffect(effect: UIEffect) {}
}

open class UIEffect
open class UIAction
open class State

interface StoreProvider<T : State> {
    fun provide(): Store<T>
}

