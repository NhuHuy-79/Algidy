package com.nhuhuy.algidy.core.model

import java.io.IOException

sealed interface UiResult<out T> {
    data object Loading : UiResult<Nothing>
    data class Success<T>(val data: T) : UiResult<T>
    data class Failure(val error: UiError) : UiResult<Nothing>
    data object Idle : UiResult<Nothing>
}

sealed interface UiError{
    data object LostConnection: UiError
    data object Unknown : UiError
}

fun <T> NetworkResult<T>.toUiStateResult(): UiResult<T> {
    return when (this) {
        is NetworkResult.Failure -> UiResult.Failure(throwable.toUiError())
        is NetworkResult.Loading -> UiResult.Loading
        is NetworkResult.Success -> UiResult.Success(data)
    }
}

fun Throwable.toUiError(): UiError = when (this) {
    is IOException -> UiError.LostConnection
    else -> UiError.Unknown
}