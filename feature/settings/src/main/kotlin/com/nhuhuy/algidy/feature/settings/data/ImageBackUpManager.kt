package com.nhuhuy.algidy.feature.settings.data

import android.content.Context
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

interface ImageBackUpManager {
    fun pickImagesToZip(
        uriList: List<String>,
        zos: ZipOutputStream
    )

    fun extractImageFromZip(zis: ZipInputStream, entryName: String, targetFolder: File)
}

class ImageBackUpManagerImpl(
    private val context: Context
) : ImageBackUpManager {
    override fun pickImagesToZip(
        uriList: List<String>,
        zos: ZipOutputStream
    ) {
        val resolver = context.contentResolver

        uriList.forEach { uriString ->
            runCatching {
                val imgUri = uriString.toUri()
                val fileName = imgUri.lastPathSegment ?: "image_${System.currentTimeMillis()}.jpg"

                val imgEntry = ZipEntry("Algidy/Image/$fileName")
                zos.putNextEntry(imgEntry)

                resolver.openInputStream(imgUri)?.use { inputStream ->
                    inputStream.copyTo(zos)
                }
                zos.closeEntry()
            }.onFailure { e ->
                println("Log_Algidy: Lỗi nén ảnh $uriString - ${e.message}")
            }
        }
    }

    override fun extractImageFromZip(zis: ZipInputStream, entryName: String, targetFolder: File) {
        runCatching {
            val fileName = File(entryName).name
            val targetImageFile = File(targetFolder, fileName)

            FileOutputStream(targetImageFile).use { fos ->
                zis.copyTo(fos)
            }
        }
    }
}
