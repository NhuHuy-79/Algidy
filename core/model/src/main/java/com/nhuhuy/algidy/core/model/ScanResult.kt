package com.nhuhuy.algidy.core.model

data class ScanResult(
    val rawText: String,
    val detectedItems: List<PendingItem>,
)

data class PendingItem(
    val name: String,
    val quantity: Double,
    val itemUnit: ItemUnit,
    val confidence: Float,
    val estimatedExpiryDays: Int
)