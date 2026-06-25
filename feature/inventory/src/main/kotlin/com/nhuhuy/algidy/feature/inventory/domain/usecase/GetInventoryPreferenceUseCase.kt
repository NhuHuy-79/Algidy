package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import kotlinx.coroutines.flow.Flow

class GetInventoryPreferenceUseCase(
    private val settingsDataStore: SettingsDataStore
) {
    fun observe(): Flow<Int> {
        return settingsDataStore.appVersionToNotifyFlow
    }

    suspend fun setVersion(version: Int) {
        settingsDataStore.setAppVersionToNotify(version)
    }
}