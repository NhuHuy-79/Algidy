package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel

@Stable
sealed interface InventoryCategoryAction : InventoryAction {
    data object OpenCreate : InventoryCategoryAction
    data object OpenEdit : InventoryCategoryAction
    data class InputChange(val input: String) : InventoryCategoryAction
    data object AddCategory : InventoryCategoryAction
    data object EditCategory : InventoryCategoryAction
    data object Delete : InventoryCategoryAction
    data class Select(val categoryUiModel: CategoryUiModel) : InventoryCategoryAction
}