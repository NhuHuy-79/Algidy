package com.nhuhuy.algidy.feature.settings.data

import android.content.Context
import androidx.core.net.toUri
import timber.log.Timber
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

interface ImageZipHandler {
    fun writeImages(
        uriList: List<String>,
        zipOutputStream: ZipOutputStream,
    )

    fun extractImage(
        zipInputStream: ZipInputStream,
        entryName: String,
        targetFolder: File,
    )
}

class ImageZipHandlerImpl(
    private val context: Context,
) : ImageZipHandler {

    override fun writeImages(
        uriList: List<String>,
        zipOutputStream: ZipOutputStream,
    ) {
        val resolver = context.contentResolver

        uriList.forEach { uriString ->
            runCatching {
                val uri = uriString.toUri()
                val fileName =
                    uri.lastPathSegment
                        ?: "image_${System.currentTimeMillis()}.jpg"

                resolver.openInputStream(uri)?.use { inputStream ->
                    zipOutputStream.putNextEntry(
                        ZipEntry("Algidy/Image/$fileName")
                    )

                    inputStream.copyTo(zipOutputStream)

                    zipOutputStream.closeEntry()
                }
            }.onFailure { error ->
                Timber.e(
                    error, "Algidy: Failed to zip image " +
                            "$uriString - ${error.message}"
                )
            }
        }
    }

    override fun extractImage(
        zipInputStream: ZipInputStream,
        entryName: String,
        targetFolder: File,
    ) {
        runCatching {
            val fileName = File(entryName).name
            val targetFile = File(targetFolder, fileName)

            targetFile.outputStream().use { output ->
                zipInputStream.copyTo(output)
            }
        }.onFailure { error ->
            Timber.e(
                error, "Algidy: Failed to extract image " +
                        "$entryName - ${error.message}"
            )
        }
    }
}