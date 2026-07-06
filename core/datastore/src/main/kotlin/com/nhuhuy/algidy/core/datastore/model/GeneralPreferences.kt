package com.nhuhuy.algidy.core.datastore.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nhuhuy.algidy.core.datastore.utils.BaseDataStore
import com.nhuhuy.algidy.core.datastore.utils.decodeFromFlow
import com.nhuhuy.algidy.core.datastore.utils.encodeToDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class GeneralPreferences(
    val appVersionToNotify: Int = 1,
    val isCameraPolicyAccepted: Boolean = false,
) {
    companion object {
        val preferencesKey = stringPreferencesKey("general_prefs")
    }
}

class GeneralDataStore(
    private val json: Json,
    private val dataStore: DataStore<Preferences>
) : BaseDataStore<GeneralPreferences> {
    override val preferencesFlow: Flow<GeneralPreferences>
        get() = dataStore.data.decodeFromFlow(
            json = json,
            key = GeneralPreferences.preferencesKey,
            defaultValue = GeneralPreferences()
        )

    override suspend fun getPreferences(): GeneralPreferences {
        return preferencesFlow.first()
    }

    override suspend fun setPreference(preferences: GeneralPreferences) {
        dataStore.encodeToDataStore(
            json = json,
            key = GeneralPreferences.preferencesKey,
            value = preferences
        )
    }
}
