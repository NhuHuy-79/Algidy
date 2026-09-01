package com.nhuhuy.algidy.feature.settings.data.import

import java.io.File
import java.util.zip.ZipInputStream

interface ImageZipImporter {
    fun extractImage(
        zipInputStream: ZipInputStream,
        entryName: String,
        targetFolder: File,
    )
}

class ImageZipImporterImpl : ImageZipImporter {

    override fun extractImage(
        zipInputStream: ZipInputStream,
        entryName: String,
        targetFolder: File,
    ) {
        val fileName = File(entryName).name
        val targetFile = File(targetFolder, fileName)

        targetFile.outputStream().use { output ->
            zipInputStream.copyTo(output)
        }
    }
}