package com.nhuhuy.algidy.core.datastore.migration

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.nhuhuy.algidy.core.datastore.UserPreferencesKeys
import com.nhuhuy.algidy.core.datastore.model.GeneralDataStore
import com.nhuhuy.algidy.core.datastore.model.GeneralPreferences
import kotlinx.coroutines.flow.first

class GeneralMigrator(
    private val dataStore: DataStore<Preferences>,
    private val generalDataStore: GeneralDataStore
) : BaseMigrator {
    override suspend fun shouldMigrate(): Boolean {
        val oldPrefs = dataStore.data.first()
        return oldPrefs.run {
            contains(UserPreferencesKeys.APP_VERSION_TO_NOTIFY)
            contains(UserPreferencesKeys.CAMERA_POLICY_ACCEPTED)
        }
    }

    override suspend fun migrate() {
        val oldPrefs = dataStore.data.first()
        val currentData = generalDataStore.getPreferences()
        val newData = GeneralPreferences(
            appVersionToNotify = oldPrefs[UserPreferencesKeys.APP_VERSION_TO_NOTIFY]
                ?: currentData.appVersionToNotify,
            isCameraPolicyAccepted = oldPrefs[UserPreferencesKeys.CAMERA_POLICY_ACCEPTED]
                ?: currentData.isCameraPolicyAccepted
        )
        generalDataStore.setPreference(newData)
    }

    override suspend fun cleanUp() {
        dataStore.edit {
            it.remove(UserPreferencesKeys.APP_VERSION_TO_NOTIFY)
            it.remove(UserPreferencesKeys.CAMERA_POLICY_ACCEPTED)
        }
    }
}