package com.nhuhuy.algidy.feature.settings.data.import

import android.content.Context
import com.nhuhuy.algidy.core.data.repository.FOLDER_IMAGE
import com.nhuhuy.algidy.feature.settings.data.model.ExportData
import com.nhuhuy.algidy.feature.settings.domain.DataImporter
import com.nhuhuy.algidy.feature.settings.domain.repository.DatabaseDataImporter
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.util.zip.ZipInputStream

class ZipDataImporter(
    private val databaseDataImporter: DatabaseDataImporter,
    private val imageZipImporter: ImageZipImporter,
    private val context: Context,
    private val json: Json,
) : DataImporter {

    override suspend fun import(
        inputStream: InputStream,
    ) {
        val imageFolder = File(
            context.filesDir,
            FOLDER_IMAGE,
        ).apply {
            mkdirs()
        }

        ZipInputStream(inputStream).use { zipInputStream ->

            var entry = zipInputStream.nextEntry

            while (entry != null) {
                if (!entry.isDirectory) {
                    when {
                        entry.name.startsWith("Algidy/Data/") -> {
                            val jsonString = zipInputStream
                                .bufferedReader()
                                .readText()

                            val exportData = json.decodeFromString<ExportData>(jsonString)
                            databaseDataImporter.import(exportData)
                        }

                        entry.name.startsWith("Algidy/Image/") -> {
                            imageZipImporter.extractImage(
                                zipInputStream = zipInputStream,
                                entryName = entry.name,
                                targetFolder = imageFolder,
                            )
                        }
                    }
                }

                zipInputStream.closeEntry()
                entry = zipInputStream.nextEntry
            }
        }
    }
}