package com.nhuhuy.algidy.core.presentation

import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.model.error_handling.Resource
import java.io.IOException

@Stable
sealed interface UiResult<out T> {
    data object Loading : UiResult<Nothing>
    data class Success<T>(val data: T) : UiResult<T>
    data class Failure(val error: UiError) : UiResult<Nothing>
    data object Idle : UiResult<Nothing>
}

@Stable
sealed interface UiError{
    data object LostConnection: UiError
    data object Unknown : UiError
}

fun <T> UiResult<T>.getDataOrNull(): T? {
    return if (this is UiResult.Success) data else null
}

fun <T> Resource<T>.toUiResult(): UiResult<T> {
    return when (this) {
        is Resource.Success -> UiResult.Success(data)
        is Resource.Failure -> UiResult.Failure(throwable.toUiError())
        Resource.Loading -> UiResult.Loading
    }
}

fun Throwable.toUiError(): UiError = when (this) {
    is IOException -> UiError.LostConnection
    else -> UiError.Unknown
}
