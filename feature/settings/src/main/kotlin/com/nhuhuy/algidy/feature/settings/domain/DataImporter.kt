package com.nhuhuy.algidy.feature.settings.domain

import java.io.InputStream

interface DataImporter {
    suspend fun import(inputStream: InputStream)
}