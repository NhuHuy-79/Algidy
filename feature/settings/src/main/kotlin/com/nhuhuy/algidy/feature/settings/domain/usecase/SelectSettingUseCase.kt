package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.notifications.worker.WorkerScheduler

class SelectSettingUseCase(
    private val settingsDataStore: SettingsDataStore,
    private val workerScheduler: WorkerScheduler
) {
    suspend fun selectDarkMode(darkMode: DarkMode) {
        settingsDataStore.setDarkMode(darkMode)
    }
    suspend fun selectAppFont(appFont: AppFont) {
        settingsDataStore.setFont(appFont)
    }

    suspend fun selectDayWarning(day: Int) {
        settingsDataStore.setWarningDay(day)
    }

    suspend fun selectAppLanguage(language: AppLanguage) {
        settingsDataStore.setLanguage(language)
    }
    suspend fun selectNotifyTime(hour: Int, minutes: Int) {
        settingsDataStore.setHour(hour)
        settingsDataStore.setMinute(minutes)
        workerScheduler.scheduleCheckExpiryWorker()
    }

    suspend fun selectDeleteThresholdDays(day: Int) {
        settingsDataStore.setDeleteThresholdDays(day)
        workerScheduler.scheduleWeeklyDeleteFoodWorker()
    }
}