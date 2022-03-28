package com.marleyspoon.listing.udf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marleyspoon.base_android.AsyncValue
import com.marleyspoon.base_android.FailureType
import kotlinx.coroutines.*

abstract class StoreViewModel<S : State>(protected val store: Store<S>) : ViewModel(),
    StoreProvider<S> {

    override fun provide(): Store<S> = store

    abstract fun onUserAction(action: UIAction)

    protected fun setState(block: S.() -> S) {
        store.setState(block)
    }

    protected fun <T : Any?> (suspend () -> T).execute(
        dispatcher: CoroutineDispatcher? = null,
        reducer: S.(AsyncValue<T>) -> S
    ): Job {
        return viewModelScope.launch(dispatcher ?: Dispatchers.IO) {
            try {
                setState { reducer(AsyncValue.Loading) }
                val result = invoke()
                setState { reducer(AsyncValue.Success(result)) }
            } catch (e: CancellationException) {
                @Suppress("RethrowCaughtException")
                throw e
            } catch (e: Throwable) {
                // TODO log ex
                setState { reducer(AsyncValue.Error(FailureType.UnknownFailure(e))) }
            }
        }
    }
}