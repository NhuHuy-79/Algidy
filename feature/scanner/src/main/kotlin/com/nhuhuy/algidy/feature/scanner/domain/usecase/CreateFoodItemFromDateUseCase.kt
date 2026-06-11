package com.nhuhuy.algidy.feature.scanner.domain.usecase

import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate
import java.text.SimpleDateFormat
import java.util.Locale

class CreateFoodItemFromDateUseCase {

    operator fun invoke(foodDate: FoodDate): Resource<FoodItem> {
        val productionTimestamp =
            foodDate.productionDate?.let { parseToLong(it) } ?: System.currentTimeMillis()
        val expiryTimestamp = foodDate.expiryDate?.let { parseToLong(it) } ?: -1L

        val foodItem = FoodItem(
            purchaseDate = productionTimestamp,
            expiryDate = expiryTimestamp
        )

        return Resource.Success(foodItem)
    }

    private fun parseToLong(dateStr: String): Long? {
        val formats = listOf(
            "dd/MM/yyyy", "dd.MM.yyyy", "dd-MM-yyyy", "yyyy/MM/dd",
            "dd MMM yyyy", "dd MMM yy",
            "dd/MM/yy", "ddMMyy", "ddMMyyyy"
        )
        for (format in formats) {
            try {
                val sdf = SimpleDateFormat(format, Locale.US).apply { isLenient = false }
                return sdf.parse(dateStr)?.time
            } catch (e: Exception) {
                continue
            }
        }
        return null
    }
}
