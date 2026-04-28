package com.nhuhuy.algidy.feature.scanner.data

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.nhuhuy.algidy.feature.scanner.domain.BarcodeScanner
import kotlinx.coroutines.tasks.await
import timber.log.Timber


class MLKitBarcodeScanner(
    private val context: Context
) : BarcodeScanner {
    private val scanner = BarcodeScanning.getClient()

    override suspend fun scanFromImage(uri: Uri): String? {
        return try {
            val image = InputImage.fromFilePath(context, uri)
            val result = scanner.process(image).await()
            result.firstOrNull()?.rawValue
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e(e, "Lỗi khi quét ảnh từ URI: $uri")
            null
        }
    }
}