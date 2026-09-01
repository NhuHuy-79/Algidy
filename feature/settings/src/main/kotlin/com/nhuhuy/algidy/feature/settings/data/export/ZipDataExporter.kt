package com.nhuhuy.algidy.feature.settings.data.export

import com.nhuhuy.algidy.feature.settings.data.ImageZipHandler
import com.nhuhuy.algidy.feature.settings.domain.DataExporter
import com.nhuhuy.algidy.feature.settings.domain.repository.ExportDataProvider
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipDataExporter(
    private val exportDataProvider: ExportDataProvider,
    private val imageZipHandler: ImageZipHandler,
    private val json: Json
) : DataExporter {

    override val extension = "zip"
    override val mimeType = "application/zip"

    override suspend fun export(): ByteArray {
        val exportData = exportDataProvider.getExportData()

        return ByteArrayOutputStream().use { output ->
            ZipOutputStream(output).use { zip ->

                val json = json.encodeToString(exportData)

                zip.putNextEntry(
                    ZipEntry("Algidy/Data/food_backup.json")
                )
                zip.write(json.encodeToByteArray())
                zip.closeEntry()

                imageZipHandler.writeImages(
                    uriList = exportData.foods.mapNotNull { it.imageUri },
                    zipOutputStream = zip
                )
            }

            output.toByteArray()
        }
    }
}