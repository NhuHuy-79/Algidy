package com.nhuhuy.algidy.core.datastore.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json


fun <T> Flow<Preferences>.get(
    key: Preferences.Key<T>,
    defaultValue: T
): Flow<T> {
    return map { preferences ->
        preferences[key] ?: defaultValue
    }
}

suspend fun <T> DataStore<Preferences>.set(key: Preferences.Key<T>, value: T) {
    edit { preferences ->
        preferences[key] = value
    }
}

inline fun <reified T> Flow<Preferences>.decodeFromFlow(
    json: Json,
    key: Preferences.Key<String>,
    defaultValue: T
): Flow<T> {
    return map { preferences ->
        preferences[key]?.let { json.decodeFromString<T>(it) } ?: defaultValue
    }
}

suspend inline fun <reified T> DataStore<Preferences>.encodeToDataStore(
    json: Json,
    key: Preferences.Key<String>,
    value: T
) {
    edit { preferences ->
        preferences[key] = json.encodeToString(value)
    }
}
