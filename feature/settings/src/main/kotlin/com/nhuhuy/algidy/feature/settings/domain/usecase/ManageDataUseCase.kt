package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.feature.settings.data.DataBackUpManger

class ManageDataUseCase(
    private val dataBackUpManger: DataBackUpManger
) {
    suspend operator fun invoke() {
        dataBackUpManger.exportDataToZip()
    }

    suspend fun exportData() {
        dataBackUpManger.exportDataToZip()
    }

    suspend fun importDate(uriPath: String) {
        dataBackUpManger.restoreEverythingFromZip(uriPath)
    }
}