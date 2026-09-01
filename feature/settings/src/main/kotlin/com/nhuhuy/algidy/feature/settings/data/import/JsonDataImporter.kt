package com.nhuhuy.algidy.feature.settings.data.import

import com.nhuhuy.algidy.feature.settings.data.model.ExportData
import com.nhuhuy.algidy.feature.settings.domain.DataImporter
import com.nhuhuy.algidy.feature.settings.domain.repository.DatabaseDataImporter
import kotlinx.serialization.json.Json
import java.io.InputStream

class JsonDataImporter(
    private val json: Json,
    private val databaseDataImporter: DatabaseDataImporter,
) : DataImporter {

    override suspend fun import(
        inputStream: InputStream,
    ) {
        val jsonString = inputStream
            .bufferedReader()
            .readText()

        val exportData = json.decodeFromString<ExportData>(
            jsonString
        )

        databaseDataImporter.import(exportData)
    }
}