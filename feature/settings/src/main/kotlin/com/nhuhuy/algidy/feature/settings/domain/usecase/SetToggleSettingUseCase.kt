package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.notifications.worker.WorkerScheduler

class SetToggleSettingUseCase(
    private val settingsDataStore: SettingsDataStore,
    private val workerScheduler: WorkerScheduler
) {
    suspend fun toggleBiometricLock(enable: Boolean) {
        settingsDataStore.setBiometricLock(enable)
    }

    suspend fun toggleWeeklyReport(enabled: Boolean) {
        settingsDataStore.setWeeklyReport(enabled)
        if (enabled) {
            workerScheduler.scheduleWeeklyReportWorker()
        } else {
            workerScheduler.cancelWeeklyReportWorker()
        }
    }

    suspend fun toggleNotifications(enable: Boolean) {
        settingsDataStore.setNotificationsEnabled(enable)
        if (enable) {
            workerScheduler.scheduleCheckExpiryWorker()
        } else {
            workerScheduler.cancelCheckExpiryWorker()
        }
    }

    suspend fun toggleDynamicColor(enable: Boolean) {
        settingsDataStore.setDynamicColor(enable)
    }

    suspend fun toggleCategoryGroup(enabled: Boolean) {
        settingsDataStore.setCategoryGroup(enabled)
    }
}