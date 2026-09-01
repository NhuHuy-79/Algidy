package com.nhuhuy.algidy.feature.settings.data.export

import com.nhuhuy.algidy.feature.settings.domain.DataExporter
import com.nhuhuy.algidy.feature.settings.domain.repository.ExportDataProvider

class CsvDataExporter(
    private val exportDataProvider: ExportDataProvider
) : DataExporter {
    override val extension: String
        get() = "csv"
    override val mimeType: String
        get() = "text/csv"

    override suspend fun export(): ByteArray {
        val exportData = exportDataProvider.getExportData()
        val csv = buildString {
            appendLine(
                "id,name,category,purchaseDate,expiryDate"
            )

            exportData.foods.forEach { food ->
                appendLine(
                    listOf(
                        food.id,
                        food.name,
                        food.category?.name.orEmpty(),
                        food.purchaseDate,
                        food.expiryDate,
                    ).joinToString(",") { escapeCsv(it.toString()) }
                )
            }
        }

        return csv.toByteArray(charset = Charsets.UTF_8)
    }

    private fun escapeCsv(value: String): String {
        return if (
            value.contains(',') ||
            value.contains('"') ||
            value.contains('\n')
        ) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }
}