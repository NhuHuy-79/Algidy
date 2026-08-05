package com.nhuhuy.algidy.core.notifications.domain.usecase

import com.nhuhuy.algidy.core.datastore.model.NotificationDataStore

class GetNotificationPreferenceUseCase(
    private val notificationDataStore: NotificationDataStore
) {
    suspend operator fun invoke(): Boolean {
        return notificationDataStore.getPreferences().enableNotification
    }

    suspend fun getNotificationEnabled(): Boolean {
        return notificationDataStore.getPreferences().enableNotification
    }

    suspend fun getWeeklyReportEnabled(): Boolean {
        return notificationDataStore.getPreferences().enableWeeklyReport
    }
}