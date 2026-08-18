package com.nhuhuy.algidy.core.model.food

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.UUID

data class FoodItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val categoryId: String? = null,
    val location: StorageLocation = StorageLocation.FRIDGE,
    val purchaseDate: Long = System.currentTimeMillis(),
    val expiryDate: Long = -1,
    val imageUri: String? = null,
    val note: String = "",
    val status: FoodStatus = FoodStatus.ACTIVE,
    val resolvedDate: Long? = null,
    val category: FoodCategory? = null,
) {
    init {
        require(purchaseDate <= expiryDate) {
            error("Expiry date cannot be earlier than purchase date.")
        }
    }
    fun getFreshnessStatus(): Freshness {
        val currentTime = System.currentTimeMillis()
        val diff = expiryDate - currentTime

        val daysRemaining = diff / (24 * 60 * 60 * 1000)

        return when {
            daysRemaining < 0 -> Freshness.EXPIRED
            daysRemaining <= 3 -> Freshness.URGENT
            daysRemaining <= 7 -> Freshness.WARNING
            else -> Freshness.FRESH
        }
    }

    fun calculateFreshnessProgress(): Float {
        if (expiryDate == -1L) return 1f

        val currentTime = System.currentTimeMillis()

        if (currentTime >= expiryDate) return 0f

        if (currentTime <= purchaseDate) return 1f

        val totalDuration = (expiryDate - purchaseDate).toFloat()
        val remainingDuration = (expiryDate - currentTime).toFloat()

        return 1f - (remainingDuration / totalDuration).coerceIn(0f, 1f)
    }

    fun getRemainingDays(): Int {
        if (expiryDate == -1L) return -1

        val today = LocalDate.now()

        val expiryLocalDate = Instant
            .ofEpochMilli(expiryDate)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        return ChronoUnit.DAYS.between(
            today,
            expiryLocalDate
        ).toInt()
    }
}

enum class UnitType { WEIGHT, VOLUME, COUNT, UNDEFINED, PACKAGING }

enum class ItemUnit(val type: UnitType) {
    KG(UnitType.WEIGHT),
    GRAM(UnitType.WEIGHT),
    LITER(UnitType.VOLUME),
    PIECE(UnitType.COUNT),
    OTHER(UnitType.UNDEFINED),
    BOTTLE(UnitType.PACKAGING)
}

enum class Freshness {
    FRESH, URGENT, WARNING, EXPIRED
}
