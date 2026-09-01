package com.nhuhuy.algidy.feature.settings.data.import

/*
class CsvDataImporter(
    private val databaseDataImporter: DatabaseDataImporter,
) : DataImporter {

    override suspend fun import(
        inputStream: InputStream,
    ) {
        val foods = inputStream
            .bufferedReader()
            .useLines { lines ->
                lines
                    .drop(1)
                    .filter { it.isNotBlank() }
                    .map { parseCsvLine(it) }
                    .map { it.toFoodExportData() }
                    .toList()
            }

        databaseDataImporter.import(
            ExportData(
                schemaVersion = DatabaseConstant.SCHEMA_VERSION,
                foods = foods,
                categories = emptyList(),
            )
        )
    }
}*/
