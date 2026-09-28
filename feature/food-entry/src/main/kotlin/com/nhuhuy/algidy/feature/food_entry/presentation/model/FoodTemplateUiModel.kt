package com.nhuhuy.algidy.feature.food_entry.presentation.model

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.FoodTemplate
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.model.toFoodCategory
import com.nhuhuy.algidy.core.presentation.model.toUiModel

@Immutable
data class FoodTemplateUiModel(
    val id: Long,
    val name: String,
    val defaultExpiryDays: Int,
    val storageLocation: StorageLocation,
    val category: CategoryUiModel,
)

fun FoodTemplate.toUiModel(): FoodTemplateUiModel? {
    return FoodTemplateUiModel(
        id = id ?: return null,
        name = name,
        defaultExpiryDays = defaultExpiryDays,
        storageLocation = storageLocation,
        category = category?.toUiModel() ?: CategoryUiModel.Uncategorized
    )
}

fun FoodTemplateUiModel.toFoodTemplate(): FoodTemplate {
    return FoodTemplate(
        id = id,
        name = name,
        defaultExpiryDays = defaultExpiryDays,
        storageLocation = storageLocation,
        category = category.toFoodCategory()
    )
}
