package com.nhuhuy.algidy.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nhuhuy.algidy.core.datastore.utils.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

interface SettingsDataStore {
    val biometricLockFlow: Flow<Boolean>
    suspend fun setBiometricLock(enabled: Boolean)
}

class SettingsDataStoreImpl(private val context: Context) : SettingsDataStore {

    override val biometricLockFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.BIOMETRIC_LOCK] ?: false
    }

    override suspend fun setBiometricLock(enabled: Boolean) {
        context.dataStore.set(UserPreferencesKeys.BIOMETRIC_LOCK, enabled)
    }

}
