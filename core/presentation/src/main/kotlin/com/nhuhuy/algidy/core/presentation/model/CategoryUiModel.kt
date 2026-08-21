package com.nhuhuy.algidy.core.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.model.food.FoodCategory

@Stable
sealed interface CategoryUiModel {
    data object All : CategoryUiModel

    @Immutable
    data class ByCategory(val data: FoodCategory) : CategoryUiModel
    data object Uncategorized : CategoryUiModel
}

fun FoodCategory?.toUiModel(): CategoryUiModel {
    return this?.let {
        CategoryUiModel.ByCategory(it)
    } ?: CategoryUiModel.Uncategorized
}

fun CategoryUiModel.toFoodCategory(): FoodCategory? {
    return when (this) {
        CategoryUiModel.All -> null
        is CategoryUiModel.ByCategory -> data
        CategoryUiModel.Uncategorized -> null
    }
}

fun List<FoodCategory>.toUiModel(): List<CategoryUiModel> {
    return listOf(CategoryUiModel.All) + this.map {
        CategoryUiModel.ByCategory(it)
    } + CategoryUiModel.Uncategorized
}

