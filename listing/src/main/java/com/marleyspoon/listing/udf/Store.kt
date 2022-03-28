package com.marleyspoon.listing.udf

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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