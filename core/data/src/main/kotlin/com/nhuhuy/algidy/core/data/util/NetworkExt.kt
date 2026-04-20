package com.nhuhuy.algidy.core.data.util

import com.nhuhuy.algidy.core.network.model.NetworkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend fun <T>safeCallInIO(
    ioDispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T,
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