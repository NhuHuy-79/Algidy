package com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.validate.FoodValidator
import com.nhuhuy.algidy.core.model.validate.ValidationResult
import com.nhuhuy.algidy.core.model.validate.ValidationResult.Companion.isValid
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.food_entry.presentation.model.EntryUiModel

@Immutable
data class FoodEntryUiState(
    val entry: EntryUiModel = EntryUiModel(),
    val categoryQuery: String = "",
    val categories: List<CategoryUiModel.ByCategory> = emptyList(),
    // Current selected category model
    val currentCategory: CategoryUiModel = CategoryUiModel.All,
    // UI Overlay state
    val overlay: FoodEntryOverlay = FoodEntryOverlay.NONE
) : UiState {
    val nameValidateResult get() = FoodValidator.validateName(entry.name)
    val purchaseDateValidateResult get() = FoodValidator.validatePurchaseDate(entry.purchaseDate)
    val expiryDateValidateResult
        get() = FoodValidator.validateExpiryDate(
            expiryDate = entry.expiryDate, purchaseDate = entry.purchaseDate
        )
    val isValid
        get() : Boolean {
            return nameValidateResult.isValid() && purchaseDateValidateResult.isValid()
                    && expiryDateValidateResult.isValid()
        }
}

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
