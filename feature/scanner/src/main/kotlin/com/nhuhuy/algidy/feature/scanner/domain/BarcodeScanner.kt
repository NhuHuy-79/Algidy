package com.nhuhuy.algidy.feature.scanner.domain

import android.net.Uri

interface BarcodeScanner {
    suspend fun scanFromImage(uri: Uri): String?
}