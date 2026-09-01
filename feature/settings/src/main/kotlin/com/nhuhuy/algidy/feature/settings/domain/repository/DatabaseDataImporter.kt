package com.nhuhuy.algidy.feature.settings.domain.repository

import com.nhuhuy.algidy.feature.settings.data.model.ExportData

interface DatabaseDataImporter {
    suspend fun import(data: ExportData)
}