package com.nhuhuy.algidy.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nhuhuy.algidy.core.datastore.utils.get
import com.nhuhuy.algidy.core.datastore.utils.set
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.model.setting.toAppFont
import com.nhuhuy.algidy.core.model.setting.toAppLanguage
import com.nhuhuy.algidy.core.model.setting.toDarkMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

interface SettingsDataStore {
    val darkModeFlow: Flow<DarkMode>
    val appLanguageFlow: Flow<AppLanguage>
    val biometricLockFlow: Flow<Boolean>
    val dynamicColorFlow: Flow<Boolean>
    val notificationsEnabledFlow: Flow<Boolean>
    val appFontFlow: Flow<AppFont>
    val categoryGroupFlow: Flow<Boolean>
    val warningDayFlow: Flow<Int>
    val hourFlow: Flow<Int>
    val minuteFlow: Flow<Int>
    val weeklyReportFlow: Flow<Boolean>
    val deleteThreshold: Flow<Int>
    val cameraPolicyAcceptedFlow: Flow<Boolean>
    val appVersionToNotifyFlow: Flow<Int>
    suspend fun setWarningDay(day: Int)
    suspend fun setCategoryGroup(enabled: Boolean)
    suspend fun setFont(appFont: AppFont)
    suspend fun setLanguage(appLanguage: AppLanguage)
    suspend fun setBiometricLock(enabled: Boolean)
    suspend fun setDynamicColor(enabled: Boolean)
    suspend fun setDarkMode(darkMode: DarkMode)
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setHour(hour: Int)
    suspend fun setMinute(minute: Int)
    suspend fun setWeeklyReport(enabled: Boolean)
    suspend fun setCameraPolicyAccepted(accepted: Boolean)
    suspend fun setAppVersionToNotify(version: Int)
    suspend fun setDeleteThresholdDays(threshold: Int)
}

class SettingsDataStoreImpl(private val context: Context) : SettingsDataStore {
    override val deleteThreshold: Flow<Int>
        get() = context.dataStore.data.map { preferences ->
            preferences[UserPreferencesKeys.DELETE_THRESHOLD] ?: 0
        }

    override val appVersionToNotifyFlow: Flow<Int>
        get() = context.dataStore.data.map { preferences ->
            preferences[UserPreferencesKeys.APP_VERSION_TO_NOTIFY] ?: 1
        }

    override val weeklyReportFlow: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[UserPreferencesKeys.WEEKLY_REPORT] ?: false
        }

    override val cameraPolicyAcceptedFlow: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[UserPreferencesKeys.CAMERA_POLICY_ACCEPTED] ?: false
        }

    override val warningDayFlow: Flow<Int>
        get() = context.dataStore.data.map { preferences ->
            preferences[UserPreferencesKeys.WARNING_DAYS] ?: 3
        }

    override val categoryGroupFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.CATEGORY] ?: false
    }

    override val darkModeFlow: Flow<DarkMode> = context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.DARK_MODE].toDarkMode()
    }

    override val appLanguageFlow: Flow<AppLanguage> = context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.LANGUAGE].toAppLanguage()
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

    override val appFontFlow: Flow<AppFont> = context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.FONT].toAppFont()
    }

    override val hourFlow: Flow<Int>
        get() = context.dataStore.data.get(UserPreferencesKeys.HOUR, 7)

    override val minuteFlow: Flow<Int>
        get() = context.dataStore.data.get(UserPreferencesKeys.MINUTE, 30)

    override suspend fun setWarningDay(day: Int) {
        context.dataStore.set(UserPreferencesKeys.WARNING_DAYS, day)
    }

    override suspend fun setMinute(minute: Int) {
        context.dataStore.set(UserPreferencesKeys.MINUTE, minute)
    }

    override suspend fun setHour(hour: Int) {
        context.dataStore.set(UserPreferencesKeys.HOUR, hour)
    }

    override suspend fun setCategoryGroup(enabled: Boolean) {
        context.dataStore.set(UserPreferencesKeys.CATEGORY, enabled)
    }

    override suspend fun setLanguage(appLanguage: AppLanguage) {
        context.dataStore.set(UserPreferencesKeys.LANGUAGE, appLanguage.isoCode)
    }

    override suspend fun setBiometricLock(enabled: Boolean) {
        context.dataStore.set(UserPreferencesKeys.BIOMETRIC_LOCK, enabled)
    }

    override suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.set(UserPreferencesKeys.DYNAMIC_COLOR, enabled)
    }

    override suspend fun setDarkMode(darkMode: DarkMode) {
        context.dataStore.set(UserPreferencesKeys.DARK_MODE, darkMode.name)
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.set(UserPreferencesKeys.NOTIFICATIONS_ENABLED, enabled)
    }

    override suspend fun setFont(appFont: AppFont) {
        context.dataStore.set(UserPreferencesKeys.FONT, appFont.storeKey)
    }

    override suspend fun setWeeklyReport(enabled: Boolean) {
        context.dataStore.set(UserPreferencesKeys.WEEKLY_REPORT, enabled)
    }

    override suspend fun setCameraPolicyAccepted(accepted: Boolean) {
        context.dataStore.set(UserPreferencesKeys.CAMERA_POLICY_ACCEPTED, accepted)
    }

    override suspend fun setAppVersionToNotify(version: Int) {
        context.dataStore.set(UserPreferencesKeys.APP_VERSION_TO_NOTIFY, version)
    }

    override suspend fun setDeleteThresholdDays(threshold: Int) {
        context.dataStore.set(UserPreferencesKeys.DELETE_THRESHOLD, threshold)
    }
}
