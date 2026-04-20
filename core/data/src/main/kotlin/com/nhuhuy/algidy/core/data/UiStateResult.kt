package com.nhuhuy.algidy.core.data

import com.nhuhuy.algidy.core.network.model.NetworkResult
import java.io.IOException

sealed interface UiStateResult<out T> {
    data object Loading : UiStateResult<Nothing>
    data class Success<T>(val data: T) : UiStateResult<T>
    data class Failure(val error: UiError) : UiStateResult<Nothing>
    data object Idle: UiStateResult<Nothing>
}

sealed interface UiError{
    data object LostConnection: UiError
    data object Unknown : UiError
}

fun <T>NetworkResult<T>.toUiStateResult(): UiStateResult<T> {
    return when (this) {
        is NetworkResult.Failure -> UiStateResult.Failure(throwable.toUiError())
        is NetworkResult.Loading -> UiStateResult.Loading
        is NetworkResult.Success -> UiStateResult.Success(data)
    }
}

fun Throwable.toUiError(): UiError = when (this) {
    is IOException -> UiError.LostConnection
    else -> UiError.Unknown
}