package com.nhuhuy.algidy.core.presentation

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

fun Throwable.toUiError(): UiError = when (this) {
    is IOException -> UiError.LostConnection
    else -> UiError.Unknown
}
