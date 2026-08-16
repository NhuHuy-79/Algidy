package com.nhuhuy.algidy.feature.inventory.utils

import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySortMode
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel

fun List<FoodUiModel>.getFilteredAndSortedList(
    category: CategoryUiModel,
    showExpiredOnly: Boolean,
    sortMode: InventorySortMode
): List<FoodUiModel> {
    val filteredByCategory = when (category) {
        CategoryUiModel.All -> this
        is CategoryUiModel.ByCategory -> this.filter {
            it.categoryId == category.data.id
        }

        CategoryUiModel.Uncategorized -> this.filter {
            it.categoryId == null
        }
    }

    val filteredByStatus = if (showExpiredOnly) {
        filteredByCategory.filter { it.freshness == Freshness.EXPIRED }
    } else {
        filteredByCategory
    }

    return when (sortMode) {
        InventorySortMode.BY_NAME -> {
            filteredByStatus.sortedBy { it.name.lowercase() }
        }

        InventorySortMode.BY_EXPIRY -> {
            filteredByStatus.sortedBy { it.expiryDate }
        }

        InventorySortMode.NONE -> {
            filteredByStatus.sortedByDescending { it.purchaseDate }
        }
    }
}

fun List<FoodUiModel>.getFilteredAndSortedList(
    pageIndex: Int,
    sortMode: InventorySortMode,
    showExpiredOnly: Boolean
): List<FoodUiModel> {
    val filteredByLocation = when (pageIndex) {
        0 -> this
        1 -> this.filter { it.location == StorageLocation.FRIDGE }
        2 -> this.filter { it.location == StorageLocation.FREEZER }
        3 -> this.filter { it.location == StorageLocation.PANTRY }
        4 -> this.filter { it.location == StorageLocation.OTHER }
        else -> this
    }

    val filteredByStatus = if (showExpiredOnly) {
        filteredByLocation.filter { it.freshness == Freshness.EXPIRED }
    } else {
        filteredByLocation
    }

    return when (sortMode) {
        InventorySortMode.BY_NAME -> {
            filteredByStatus.sortedBy { it.name.lowercase() }
        }

        InventorySortMode.BY_EXPIRY -> {
            filteredByStatus.sortedBy { it.expiryDate }
        }

        InventorySortMode.NONE -> {
            filteredByStatus.sortedByDescending { it.purchaseDate }
        }
    }
}