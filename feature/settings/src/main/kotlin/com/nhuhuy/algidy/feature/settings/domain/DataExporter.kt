package com.nhuhuy.algidy.feature.settings.domain

interface DataExporter {
    val extension: String
    val mimeType: String

    suspend fun export(): ByteArray
}