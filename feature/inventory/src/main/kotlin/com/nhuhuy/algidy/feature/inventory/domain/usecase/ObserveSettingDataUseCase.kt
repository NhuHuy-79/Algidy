package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.datastore.model.AppearanceDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveSettingDataUseCase(
    private val appearanceDataStore: AppearanceDataStore,
) {
    fun getCategoryEnabled(): Flow<Boolean> {
        return appearanceDataStore.preferencesFlow.map {
            it.enableCategoryGroup
        }
    }

}