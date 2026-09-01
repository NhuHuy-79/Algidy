package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.feature.settings.data.export.CsvDataExporter
import com.nhuhuy.algidy.feature.settings.data.export.JsonDataExporter
import com.nhuhuy.algidy.feature.settings.data.export.ZipDataExporter
import com.nhuhuy.algidy.feature.settings.domain.model.ExportFormat

class ExportDataUseCase(
    private val jsonExporter: JsonDataExporter,
    private val csvDataExporter: CsvDataExporter,
    private val zipDataExporter: ZipDataExporter,
) {
    suspend operator fun invoke(
        format: ExportFormat
    ): ByteArray {
        return when (format) {
            ExportFormat.JSON -> jsonExporter.export()
            ExportFormat.CSV -> csvDataExporter.export()
            ExportFormat.ZIP -> zipDataExporter.export()
        }
    }
}