package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import kotlinx.coroutines.flow.Flow

class ObserveSettingDataUseCase(
    private val settingsDataStore: SettingsDataStore
) {
    fun getCategoryEnabled(): Flow<Boolean> {
        return settingsDataStore.categoryGroupFlow
    }
}