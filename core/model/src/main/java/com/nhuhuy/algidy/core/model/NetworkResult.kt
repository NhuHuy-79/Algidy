package com.nhuhuy.algidy.core.model

sealed interface NetworkResult<out T> {
    data object Loading: NetworkResult<Nothing>
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Failure(val throwable: Throwable) : NetworkResult<Nothing>
}