package com.nhuhuy.algidy.feature.inventory.presentation.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.model.toUiModel
import kotlin.math.abs

@Immutable
data class FoodUiModel(
    val id: String = "",
    val imageUri: String? = null,
    val categoryId: String? = null,
    val name: String = "",
    val remainDays: Int = 0,
    val expiryDate: Long = -1L,
    val purchaseDate: Long = -1L,
    val freshness: Freshness = Freshness.FRESH,
    val location: StorageLocation = StorageLocation.OTHER,
    val note: String = "",
    val categoryUiModel: CategoryUiModel = CategoryUiModel.Uncategorized
)

fun FoodItem.toFoodUiModel(): FoodUiModel {
    return FoodUiModel(
        id = id,
        categoryId = categoryId,
        imageUri = imageUri,
        categoryUiModel = category.toUiModel(),
        name = name,
        remainDays = getRemainingDays(),
        expiryDate = expiryDate,
        purchaseDate = purchaseDate,
        freshness = getFreshnessStatus(),
        location = location,
        note = note
    )
}


fun List<FoodItem>.toFoodUiModel(): List<FoodUiModel> {
    return map { it.toFoodUiModel() }
}

@Composable
fun readableRemainDays(remainingDays: Int): String {
    val remainingDaysText = when {
        remainingDays < 0 -> stringResource(R.string.freshness_expired, abs(remainingDays))
        remainingDays == 0 -> stringResource(R.string.freshness_expires_today)
        remainingDays == 1 -> stringResource(R.string.freshness_one_day_left)
        remainingDays < 30 -> stringResource(R.string.freshness_days_left, remainingDays)
        else -> stringResource(R.string.freshness_months_left, remainingDays / 30)
    }

    return remainingDaysText
}


