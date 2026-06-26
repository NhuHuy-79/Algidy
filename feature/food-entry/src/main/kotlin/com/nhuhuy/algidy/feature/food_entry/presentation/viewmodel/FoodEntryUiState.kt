package com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.model.validate.ValidationResult
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState

@Immutable
data class FoodEntryUiState(
    val id: String = "",
    val name: String = "",
    val categoryId: String? = null,
    val categoryQuery: String = "",
    val categories: List<CategoryUiModel.ByCategory> = emptyList(),
    val location: StorageLocation = StorageLocation.FRIDGE,
    val purchaseDate: Long = System.currentTimeMillis(),
    val expiryDate: Long = -1,
    val imageUri: String? = null,
    val notes: String = "",

    // Current selected category model
    val currentCategory: CategoryUiModel = CategoryUiModel.All,

    // UI Overlay state
    val overlay: FoodEntryOverlay = FoodEntryOverlay.NONE
) : UiState

enum class FoodEntryOverlay {
    NONE,
    PURCHASE_DATE_PICKER,
    EXPIRY_DATE_PICKER,
    CATEGORY_ADD,
}


@Immutable
data class FoodEntryError(
    val nameValidation: ValidationResult = ValidationResult.IDLE,
    val expiryDateValidation: ValidationResult = ValidationResult.IDLE,
    val purchaseDateValidation: ValidationResult = ValidationResult.IDLE
) {
    val isNameError: Boolean
        get() = nameValidation !in listOf(ValidationResult.SUCCESS, ValidationResult.IDLE)

    val isPurchaseDateError: Boolean
        get() = purchaseDateValidation !in listOf(ValidationResult.SUCCESS, ValidationResult.IDLE)

    val isExpiryDateError: Boolean
        get() = expiryDateValidation !in listOf(ValidationResult.SUCCESS, ValidationResult.IDLE)

    val isValid: Boolean
        get() = !isPurchaseDateError && !isExpiryDateError && !isNameError
}
