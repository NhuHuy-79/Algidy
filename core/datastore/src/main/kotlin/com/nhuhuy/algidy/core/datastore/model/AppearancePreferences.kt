package com.nhuhuy.algidy.core.datastore.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nhuhuy.algidy.core.datastore.utils.BaseDataStore
import com.nhuhuy.algidy.core.datastore.utils.decodeFromFlow
import com.nhuhuy.algidy.core.datastore.utils.encodeToDataStore
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class AppearancePreferences(
    val darkMode: DarkMode = DarkMode.SYSTEM,
    val appLanguage: AppLanguage = AppLanguage.ENGLISH,
    val enableDynamicColor: Boolean = false,
    val enableCategoryGroup: Boolean = false,
) {
    companion object {
        val preferencesKey = stringPreferencesKey("appearance_prefs")
    }
}

class AppearanceDataStore(
    private val json: Json,
    private val dataStore: DataStore<Preferences>
) : BaseDataStore<AppearancePreferences> {
    override val preferencesFlow: Flow<AppearancePreferences>
        get() = dataStore.data.decodeFromFlow(
            json = json,
            key = AppearancePreferences.preferencesKey,
            defaultValue = AppearancePreferences()
        )

    override suspend fun getPreferences(): AppearancePreferences {
        return preferencesFlow.first()
    }

    override suspend fun setPreference(preferences: AppearancePreferences) {
        dataStore.encodeToDataStore(
            json = json,
            key = AppearancePreferences.preferencesKey,
            value = preferences
        )
    }

}
