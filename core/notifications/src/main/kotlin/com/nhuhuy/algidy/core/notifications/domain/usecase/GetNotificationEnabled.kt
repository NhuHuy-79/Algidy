package com.nhuhuy.algidy.core.notifications.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import kotlinx.coroutines.flow.first

class GetNotificationEnabled(
    private val settingsDataStore: SettingsDataStore
) {
    suspend operator fun invoke(): Boolean {
        return settingsDataStore.notificationsEnabledFlow.first()
    }
}