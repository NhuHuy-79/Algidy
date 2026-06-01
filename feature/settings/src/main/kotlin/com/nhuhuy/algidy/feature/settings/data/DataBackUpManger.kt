package com.nhuhuy.algidy.feature.settings.data

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import com.nhuhuy.algidy.core.data.FOLDER_IMAGE
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.safeCall
import com.nhuhuy.algidy.core.model.error_handling.Resource
import java.io.BufferedOutputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class DataBackUpManger(
    private val context: Context,
    private val appDispatchers: AppDispatchers,
    private val databaseBackUpManager: DatabaseBackUpManager,
    private val imageBackUpManager: ImageBackUpManager
) {
    suspend fun exportDataToZip(): Resource<String> {
        return safeCall(dispatcher = appDispatchers.io) {
            val resolver = context.contentResolver
            val zipFileName = "algidy_backup_${System.currentTimeMillis()}.zip"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, zipFileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/zip")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_DOWNLOADS}/Algidy"
                )
            }
            val targetZipUri =
                resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    ?: throw Exception("Cannot zip file")

            resolver.openOutputStream(targetZipUri)?.use { outputStream ->
                ZipOutputStream(BufferedOutputStream(outputStream)).use { zos ->

                    val jsonString = databaseBackUpManager.exportToJson()
                    val jsonEntry = ZipEntry("Algidy/Data/food_backup.json")
                    zos.putNextEntry(jsonEntry)
                    zos.write(jsonString.encodeToByteArray())
                    zos.closeEntry()

                    val imageUris = databaseBackUpManager.getAllImageUris()
                    imageBackUpManager.pickImagesToZip(imageUris, zos)
                }
            } ?: throw Exception("Cannot open file zip to write!")

            "Download/Algidy/$zipFileName"
        }
    }

    suspend fun restoreEverythingFromZip(sourceZipUriPath: String): Resource<Unit> =
        safeCall(appDispatchers.io) {
            val sourceZipUri = sourceZipUriPath.toUri()
            val resolver = context.contentResolver

            val targetInternalImageFolder = File(context.filesDir, FOLDER_IMAGE)
            if (!targetInternalImageFolder.exists()) {
                targetInternalImageFolder.mkdirs()
            }

            resolver.openInputStream(sourceZipUri)?.use { inputStream ->
                ZipInputStream(inputStream).use { zis ->
                    var entry = zis.nextEntry

                    while (entry != null) {
                        if (!entry.isDirectory) {

                            if (entry.name.contains("Algidy/Data/")) {
                                val jsonString = zis.bufferedReader().readText()
                                databaseBackUpManager.importFromJson(jsonString)
                            }

                            else if (entry.name.contains("Algidy/Image/")) {
                                imageBackUpManager.extractImageFromZip(
                                    zis = zis,
                                    entryName = entry.name,
                                    targetFolder = targetInternalImageFolder
                                )
                            }
                        }
                        zis.closeEntry()
                        entry = zis.nextEntry
                    }
                }
            } ?: throw Exception("Cannot unzip file")
        }
}