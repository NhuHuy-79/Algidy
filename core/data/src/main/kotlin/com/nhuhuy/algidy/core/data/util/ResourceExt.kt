package com.nhuhuy.algidy.core.data.util

import com.nhuhuy.algidy.core.model.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend inline fun <T> safeCallInIO(
    ioDispatcher: CoroutineDispatcher,
    crossinline apiCall: suspend () -> T,
): Resource<T> {
    return withContext(ioDispatcher){
        try {
            val data = apiCall()
            Resource.Success(data)
        } catch (e: CancellationException){
            throw e
        } catch (e: Exception){
            Timber.e(e)
            Resource.Failure(e)
        }
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

inline fun <T> Resource<T>.onFailure(
    action: (Throwable) -> Unit
): Resource<T> {
    if (this is Resource.Failure) {
        action(throwable)
    }
    return this
}