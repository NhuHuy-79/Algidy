package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.feature.settings.data.DataBackUpManger

class ManageDataUseCase(
    private val dataBackUpManger: DataBackUpManger
) {
    suspend operator fun invoke() {
        dataBackUpManger.exportDataToZip()
    }

    suspend fun exportData(): Resource<String> {
        return dataBackUpManger.exportDataToZip()
    }

    suspend fun importDate(uriPath: String): Resource<Unit> {
        return dataBackUpManger.restoreEverythingFromZip(uriPath)
    }
}