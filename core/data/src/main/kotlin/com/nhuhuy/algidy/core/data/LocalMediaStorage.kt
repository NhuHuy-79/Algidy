package com.nhuhuy.algidy.core.data

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.safeCall
import com.nhuhuy.algidy.core.model.error_handling.Resource
import java.io.File
import java.util.UUID

const val FOLDER_NAME = "food_item_images"

interface LocalMediaStorage {
    suspend fun copyImageToInternalStorage(uriPath: String): Resource<String>
    suspend fun deleteImageFromInternalStorage(uriPath: String): Resource<Unit>
}

class LocalMediaStorageImpl(
    private val context: Context,
    private val appDispatchers: AppDispatchers
) : LocalMediaStorage {
    override suspend fun copyImageToInternalStorage(uriPath: String): Resource<String> {
        return safeCall(dispatcher = appDispatchers.io) {
            val originalUri = uriPath.toUri()
            if (originalUri.scheme == "file" && uriPath.contains(context.packageName)) {
                return@safeCall uriPath
            }

            val extension = getFileExtension(originalUri) ?: "jpg"
            val uniqueFileName = "IMG_${UUID.randomUUID()}.$extension"

            val directory = File(context.filesDir, FOLDER_NAME)
            if (!directory.exists()) {
                directory.mkdirs()
            }

            val destinationFile = File(directory, uniqueFileName)

            context.contentResolver.openInputStream(originalUri)?.use { inputStream ->
                destinationFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
                ?: throw IllegalArgumentException("Cannot open input stream for the given URI: $uriPath")

            Uri.fromFile(destinationFile).toString()
        }
    }

    override suspend fun deleteImageFromInternalStorage(uriPath: String): Resource<Unit> {
        return safeCall(dispatcher = appDispatchers.io) {
            val uri = uriPath.toUri()
            val path = uri.path

            if (path == null) {
                throw IllegalArgumentException("Invalid Uri")
            }

            val file = File(path)

            if (file.exists()) {
                file.delete()
            } else {
                Unit
            }
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }
}
