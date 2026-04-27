package com.nhuhuy.algidy.core.data.util

import com.nhuhuy.algidy.core.model.NetworkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend inline fun <T> safeCallInIO(
    ioDispatcher: CoroutineDispatcher,
    crossinline apiCall: suspend () -> T,
) : NetworkResult<T>{
    return withContext(ioDispatcher){
        try {
            val data = apiCall()
            NetworkResult.Success(data)
        } catch (e: CancellationException){
            throw e
        } catch (e: Exception){
            Timber.e(e)
            NetworkResult.Failure(e)
        }
    }
}

inline fun <T> NetworkResult<T>.onSuccess(
    action: (T) -> Unit
) : NetworkResult<T>{
    if (this is NetworkResult.Success){
        action(data)
    }
    return this
}

suspend inline fun <T> NetworkResult<T>.onSuspendSuccess(
    action: suspend (T) -> Unit
): NetworkResult<T> {
    if (this is NetworkResult.Success) {
        action(data)
    }
    return this
}

inline fun <T> NetworkResult<T>.onFailure(
    action: (Throwable) -> Unit
) : NetworkResult<T>{
    if (this is NetworkResult.Failure){
        action(throwable)
    }
    return this
}