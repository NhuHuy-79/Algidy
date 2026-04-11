package com.nhuhuy.algidy.core.model

data class FoodItem(
    val id: String,
    val name: String,
    val categoryId: String,
    val location: StorageLocation,
    val quantity: Double,
    val itemUnit: ItemUnit,
    val purchaseDate: Long = System.currentTimeMillis(),
    val expiryDate: Long,
    val imageUri: String? = null,
    val isFavorite: Boolean = false,
    val notes: String = ""
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