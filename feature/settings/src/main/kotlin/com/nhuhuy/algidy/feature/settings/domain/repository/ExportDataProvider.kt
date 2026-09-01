package com.nhuhuy.algidy.feature.settings.domain.repository

import com.nhuhuy.algidy.feature.settings.data.model.ExportData

interface ExportDataProvider {
    suspend fun getExportData(): ExportData
}