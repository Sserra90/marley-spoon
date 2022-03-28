package com.marleyspoon.base_android

sealed class AsyncValue<out T> {
    data class Success<T>(val data: T) : AsyncValue<T>()
    data class Error(val failure: FailureType) : AsyncValue<Nothing>()
    object Loading : AsyncValue<Nothing>()
    object None : AsyncValue<Nothing>()
}

sealed class FailureType {
    data class UnknownFailure(val error: Throwable) : FailureType()
    object NetworkFailure : FailureType()
    data class ServerFailure(val error: String) : FailureType()
    data class FeatureFailure<T>(val failure: T) : FailureType()
}
