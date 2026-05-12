package com.nhuhuy.algidy.core.presentation

import com.nhuhuy.algidy.core.model.error_handling.Resource
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

fun <T> Resource<T>.toUiStateResult(): UiResult<T> {
    return when (this) {
        is Resource.Failure -> UiResult.Failure(throwable.toUiError())
        is Resource.Loading -> UiResult.Loading
        is Resource.Success -> UiResult.Success(data)
    }
}

fun Throwable.toUiError(): UiError = when (this) {
    is IOException -> UiError.LostConnection
    else -> UiError.Unknown
}
