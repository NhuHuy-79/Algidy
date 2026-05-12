package com.nhuhuy.algidy.core.data.util

import com.nhuhuy.algidy.core.model.error_handling.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend inline fun <T> safeCall(
    dispatcher: CoroutineDispatcher,
    noinline onFinally: () -> Unit = {},
    crossinline apiCall: suspend () -> T
): Resource<T> {
    return withContext(dispatcher) {
        try {
            val data = apiCall()
            Resource.Success(data)
        } catch (e: CancellationException){
            throw e
        } catch (e: Exception){
            Timber.e(e)
            Resource.Failure(e)
        } finally {
            onFinally()
        }
    }
}

inline fun <T, R> Resource<T>.map(
    transform: (T) -> R
): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(transform(data))
        is Resource.Failure -> Resource.Failure(throwable)
        Resource.Loading -> Resource.Loading
    }
}

inline fun <T> Resource<T>.onSuccess(
    action: (T) -> Unit
): Resource<T> {
    if (this is Resource.Success) {
        action(data)
    }
    return this
}

fun <T> Resource<T>.getDataOrNull(): T? {
    return if (this is Resource.Success) data else null
}

suspend inline fun <T> Resource<T>.onSuspendSuccess(
    action: suspend (T) -> Unit
): Resource<T> {
    if (this is Resource.Success) {
        action(data)
    }
    return this
}

suspend inline fun <T> Resource<T>.onSuspendFailure(
    action: suspend (Throwable) -> Unit
): Resource<T> {
    if (this is Resource.Failure) {
        action(throwable)
    }
    return this
}

inline fun <T> Resource<T>.onFailure(
    action: (Throwable) -> Unit
): Resource<T> {
    if (this is Resource.Failure) {
        action(throwable)
    }
    return this
}
