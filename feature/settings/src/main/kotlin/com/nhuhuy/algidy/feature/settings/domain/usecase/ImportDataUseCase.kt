package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.feature.settings.data.import.JsonDataImporter
import com.nhuhuy.algidy.feature.settings.data.import.ZipDataImporter
import com.nhuhuy.algidy.feature.settings.domain.model.ImportFormat
import java.io.InputStream

class ImportDataUseCase(
    private val jsonDataImporter: JsonDataImporter,
    private val zipDataImporter: ZipDataImporter
) {
    suspend operator fun invoke(
        inputStream: InputStream,
        importFormat: ImportFormat
    ) {
        return when (importFormat) {
            ImportFormat.JSON -> jsonDataImporter.import(inputStream)
            ImportFormat.ZIP -> zipDataImporter.import(inputStream)
        }
    }
}