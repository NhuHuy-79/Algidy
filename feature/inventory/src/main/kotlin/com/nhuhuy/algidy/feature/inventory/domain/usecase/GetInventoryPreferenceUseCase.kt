package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.datastore.model.GeneralDataStore
import com.nhuhuy.algidy.core.datastore.model.GeneralPreferences
import kotlinx.coroutines.flow.Flow

class GetInventoryPreferenceUseCase(
    private val generalDataStore: GeneralDataStore
) {
    fun observe(): Flow<GeneralPreferences> {
        return generalDataStore.preferencesFlow
    }

    suspend fun updatePreferences(generalPreferences: GeneralPreferences) {
        generalDataStore.setPreference(generalPreferences)
    }

}