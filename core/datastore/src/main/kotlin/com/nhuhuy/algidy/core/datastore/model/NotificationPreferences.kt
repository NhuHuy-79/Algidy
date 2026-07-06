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
data class NotificationPreferences(
    val deleteFoodThreshold: Int = 0,
    val warningFoodThreshold: Int = 0,
    val hour: Int = 7,
    val minutes: Int = 30,
    val enableWeeklyReport: Boolean = true,
    val enableNotification: Boolean = false,
) {
    companion object {
        val preferencesKey = stringPreferencesKey("notification_prefs")
    }
}

class NotificationDataStore(
    private val json: Json,
    private val dataStore: DataStore<Preferences>
) : BaseDataStore<NotificationPreferences> {
    override val preferencesFlow: Flow<NotificationPreferences>
        get() = dataStore.data.decodeFromFlow(
            json = json,
            key = NotificationPreferences.preferencesKey,
            defaultValue = NotificationPreferences()
        )

    override suspend fun getPreferences(): NotificationPreferences = preferencesFlow.first()

    override suspend fun setPreference(preferences: NotificationPreferences) {
        dataStore.encodeToDataStore(
            json = json,
            key = NotificationPreferences.preferencesKey,
            value = preferences
        )
    }

}