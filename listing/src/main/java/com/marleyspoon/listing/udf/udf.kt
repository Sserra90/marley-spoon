package com.marleyspoon.listing.udf

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


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

abstract class StoreViewModel<S : State>(protected val store: Store<S>) : ViewModel(),
    StoreProvider<S> {

    override fun provide(): Store<S> = store

    abstract fun onUserAction(action: UIAction)

    protected fun setState(block: (s: S) -> S) {
        store.setState(block)
    }
}

class Store<T : State>(private val initialState: T) {

    class Binder<S : State> {
        fun bind(renderable: Renderable<S>, provider: StoreProvider<S>) {
            provider.provide().let {
                renderable.lifecycleScope.launch {
                    renderable.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        it.observe(this) { s -> renderable.render(s) }
                    }
                }
            }
        }
    }

    private val stateFlow: MutableStateFlow<T> by lazy { MutableStateFlow(initialState) }

    fun setState(action: (currState: T) -> T) {
        stateFlow.value = action(stateFlow.value)
    }

    fun observe(scope: CoroutineScope, observer: (state: T) -> Unit) {
        stateFlow.onEach { observer(it) }.launchIn(scope)
    }

}
