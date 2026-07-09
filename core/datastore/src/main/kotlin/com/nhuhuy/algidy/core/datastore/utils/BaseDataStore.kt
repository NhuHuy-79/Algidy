package com.nhuhuy.algidy.core.datastore.utils

import kotlinx.coroutines.flow.Flow

interface BaseDataStore<T> {
    val preferencesFlow: Flow<T>
    suspend fun getPreferences(): T
    suspend fun setPreference(preferences: T)
}