package com.nhuhuy.algidy.feature.settings.data.export

import com.nhuhuy.algidy.feature.settings.domain.DataExporter
import com.nhuhuy.algidy.feature.settings.domain.repository.ExportDataProvider
import kotlinx.serialization.json.Json

class JsonDataExporter(
    private val json: Json,
    private val exportDataProvider: ExportDataProvider,
) : DataExporter {
    override val extension: String
        get() = "json"
    override val mimeType: String
        get() = "application/json"

    override suspend fun export(): ByteArray {
        val exportData = exportDataProvider.getExportData()
        return json.encodeToString(exportData).toByteArray(charset = Charsets.UTF_8)
    }

}