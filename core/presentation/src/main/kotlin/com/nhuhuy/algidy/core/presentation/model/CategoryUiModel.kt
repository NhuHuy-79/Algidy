package com.nhuhuy.algidy.core.presentation.model

import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.model.food.FoodCategory

@Stable
sealed interface CategoryUiModel {
    data object All : CategoryUiModel
    data class ByCategory(val data: FoodCategory) : CategoryUiModel
}


fun List<FoodCategory>.toUiModel(): List<CategoryUiModel> {
    return listOf(CategoryUiModel.All) + this.map {
        CategoryUiModel.ByCategory(it)
    }
}

fun List<FoodCategory>.toByCategoryModel(): List<CategoryUiModel.ByCategory> {
    return this.map {
        CategoryUiModel.ByCategory(it)
    }
}