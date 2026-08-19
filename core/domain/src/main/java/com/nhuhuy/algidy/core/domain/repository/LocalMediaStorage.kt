package com.nhuhuy.algidy.core.domain.repository

import com.nhuhuy.algidy.core.model.error_handling.Resource

interface LocalMediaStorage {
    suspend fun copyImageToInternalStorage(uriPath: String): Resource<String>
    suspend fun deleteImageFromInternalStorage(uriPath: String): Resource<Unit>
    fun deleteAllFromInternalStorage()
    suspend fun getAllUriPath(): Resource<List<String>>

}