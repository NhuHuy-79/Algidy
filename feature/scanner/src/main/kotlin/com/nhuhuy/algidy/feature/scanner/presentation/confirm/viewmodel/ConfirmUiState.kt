package com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.ValidationResult

@Immutable
data class ConfirmUiState(
    val foodItem: FoodItem = FoodItem(),
    val expandedUnitMenu: Boolean = false,
    val overlay: ConfirmOverlay = ConfirmOverlay.NONE,
    val errorState: ConfirmError = ConfirmError()
)

@Immutable
data class ConfirmError(
    val nameValidation: ValidationResult = ValidationResult.IDLE,
    val quantityValidation: ValidationResult = ValidationResult.IDLE,
    val expiryDateValidation: ValidationResult = ValidationResult.IDLE,
    val purchaseDateValidation: ValidationResult = ValidationResult.IDLE
) {
    val isNameError: Boolean
        get() = nameValidation !in listOf(
            ValidationResult.SUCCESS,
            ValidationResult.IDLE
        )
    val isQuantityError: Boolean
        get() = quantityValidation !in listOf(
            ValidationResult.SUCCESS,
            ValidationResult.IDLE
        )
    val isPurchaseDateError: Boolean
        get() = purchaseDateValidation !in listOf(
            ValidationResult.SUCCESS,
            ValidationResult.IDLE
        )
    val isExpiryDateError: Boolean
        get() = expiryDateValidation !in listOf(
            ValidationResult.SUCCESS,
            ValidationResult.IDLE
        )
    val valid: Boolean
        get() = nameValidation == ValidationResult.SUCCESS &&
                quantityValidation == ValidationResult.SUCCESS &&
                expiryDateValidation == ValidationResult.SUCCESS &&
                purchaseDateValidation == ValidationResult.SUCCESS

}

enum class ConfirmOverlay {
    NONE, EXPIRY_DATE_PICKER, PURCHASE_DATE_PICKER, ERROR_DIALOG, EXIT_DIALOG
}
