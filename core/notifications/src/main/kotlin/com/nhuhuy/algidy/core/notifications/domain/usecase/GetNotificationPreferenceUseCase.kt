package com.nhuhuy.algidy.core.notifications.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import kotlinx.coroutines.flow.first

class GetNotificationPreferenceUseCase(
    private val settingsDataStore: SettingsDataStore
) {
    suspend operator fun invoke(): Boolean {
        return settingsDataStore.notificationsEnabledFlow.first()
    }

    suspend fun getNotificationEnabled(): Boolean {
        return settingsDataStore.notificationsEnabledFlow.first()
    }

    suspend fun getWeeklyReportEnabled(): Boolean {
        return settingsDataStore.weeklyReportFlow.first()
    }
}