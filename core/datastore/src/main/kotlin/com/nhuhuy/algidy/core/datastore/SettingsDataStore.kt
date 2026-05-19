package com.nhuhuy.algidy.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.model.setting.toDarkMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

interface SettingsDataStore {
    val darkModeFlow: Flow<DarkMode>
    val languageFlow: Flow<String>
    val biometricLockFlow: Flow<Boolean>
    val dynamicColorFlow: Flow<Boolean>
    val notificationsEnabledFlow: Flow<Boolean>

    suspend fun setLanguage(language: String)
    suspend fun setBiometricLock(enabled: Boolean)
    suspend fun setDynamicColor(enabled: Boolean)
    suspend fun setDarkMode(darkMode: DarkMode)
    suspend fun setNotificationsEnabled(enabled: Boolean)
}

class SettingsDataStoreImpl(private val context: Context) : SettingsDataStore {

    override val darkModeFlow: Flow<DarkMode> = context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.DARK_MODE].toDarkMode()
    }

    override val languageFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.LANGUAGE] ?: "en"
    }

    override val biometricLockFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.BIOMETRIC_LOCK] ?: false
    }

    override val dynamicColorFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.DYNAMIC_COLOR] ?: false
    }

    override val notificationsEnabledFlow: Flow<Boolean> =
        context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
    }

    override suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.LANGUAGE] = language
        }
    }

    override suspend fun setBiometricLock(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.BIOMETRIC_LOCK] = enabled
        }
    }

    override suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.DYNAMIC_COLOR] = enabled
        }
    }

    override suspend fun setDarkMode(darkMode: DarkMode) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.DARK_MODE] = darkMode.name
        }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }
}
