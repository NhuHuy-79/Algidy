package com.nhuhuy.algidy.core.datastore.migration

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.nhuhuy.algidy.core.datastore.UserPreferencesKeys
import com.nhuhuy.algidy.core.datastore.model.AppearanceDataStore
import com.nhuhuy.algidy.core.datastore.model.AppearancePreferences
import com.nhuhuy.algidy.core.model.setting.toAppLanguage
import com.nhuhuy.algidy.core.model.setting.toDarkMode
import kotlinx.coroutines.flow.first

class AppearanceMigrator(
    private val dataStore: DataStore<Preferences>,
    private val appearanceDataStore: AppearanceDataStore,
) : BaseMigrator {
    override suspend fun shouldMigrate(): Boolean {
        val prefs = dataStore.data.first()

        return prefs.run {
            contains(UserPreferencesKeys.DYNAMIC_COLOR) ||
                    contains(UserPreferencesKeys.DARK_MODE) ||
                    contains(UserPreferencesKeys.LANGUAGE) ||
                    contains(UserPreferencesKeys.CATEGORY)
        }
    }

    override suspend fun migrate() {
        val oldPrefs = dataStore.data.first()
        val currentData = appearanceDataStore.getPreferences()
        val newData = AppearancePreferences(
            darkMode = oldPrefs[UserPreferencesKeys.DARK_MODE].toDarkMode(),
            enableDynamicColor = oldPrefs[UserPreferencesKeys.DYNAMIC_COLOR]
                ?: currentData.enableDynamicColor,
            appLanguage = oldPrefs[UserPreferencesKeys.LANGUAGE].toAppLanguage(),
            enableCategoryGroup = oldPrefs[UserPreferencesKeys.CATEGORY]
                ?: currentData.enableCategoryGroup
        )
        appearanceDataStore.setPreference(newData)
    }

    override suspend fun cleanUp() {
        dataStore.edit {
            it.remove(UserPreferencesKeys.DARK_MODE)
            it.remove(UserPreferencesKeys.CATEGORY)
            it.remove(UserPreferencesKeys.DYNAMIC_COLOR)
            it.remove(UserPreferencesKeys.LANGUAGE)
        }
    }

}