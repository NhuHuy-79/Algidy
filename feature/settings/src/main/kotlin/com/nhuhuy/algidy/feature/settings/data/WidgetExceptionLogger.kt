package com.nhuhuy.algidy.feature.settings.data

import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

interface WidgetExceptionLogger {
    suspend fun log(throwable: Throwable, glanceId: String)
    fun latestLog(): Flow<String?>
    suspend fun clear()
}


class WidgetExceptionLoggerImpl(
    private val dataStore: DataStore<Preferences>
) : WidgetExceptionLogger {

    override suspend fun log(
        throwable: Throwable,
        glanceId: String,
    ) {
        dataStore.edit { prefs ->
            prefs[WidgetLogKeys.LAST_EXCEPTION] =
                buildString {
                    appendLine("Android=${Build.VERSION.SDK_INT}")
                    appendLine("Manufacturer=${Build.MANUFACTURER}")
                    appendLine("Model=${Build.MODEL}")
                    appendLine("GlanceId: $glanceId")
                    appendLine(Instant.now())
                    appendLine(throwable.message)
                    appendLine(throwable.stackTraceToString())
                }
        }
    }

    override fun latestLog(): Flow<String?> =
        dataStore.data.map { it[WidgetLogKeys.LAST_EXCEPTION] }

    override suspend fun clear() {
        dataStore.edit {
            it.remove(
                WidgetLogKeys.LAST_EXCEPTION
            )
        }
    }
}

private object WidgetLogKeys {
    val LAST_EXCEPTION =
        stringPreferencesKey(
            "widget_last_exception"
        )
}