package com.nhuhuy.algidy.core.model

sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Failure(val throwable: Throwable) : Resource<Nothing>
}