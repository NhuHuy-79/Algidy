package com.nhuhuy.algidy.core.model.food

import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.math.abs

@Serializable
data class FoodItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val categoryId: String = "",
    val defaultFoodCategory: DefaultFoodCategory = DefaultFoodCategory.OTHERS,
    val location: StorageLocation = StorageLocation.FRIDGE,
    val quantity: Double = 0.0,
    val itemUnit: ItemUnit = ItemUnit.KG,
    val purchaseDate: Long = System.currentTimeMillis(),
    val expiryDate: Long = -1,
    val imageUri: String? = null,
    val isFavorite: Boolean = false,
    val notes: String = "",
    val status: FoodStatus = FoodStatus.ACTIVE,
    val resolvedDate: Long? = null
) {
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

        return (remainingDuration / totalDuration).coerceIn(0f, 1f)
    }

    fun getRemainingDays(): Int {
        if (expiryDate == -1L) return -1

        val currentTime = System.currentTimeMillis()
        val diff = expiryDate - currentTime
        return (diff / (24 * 60 * 60 * 1000)).toInt()
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
    EXPIRED, URGENT, WARNING, FRESH
}
