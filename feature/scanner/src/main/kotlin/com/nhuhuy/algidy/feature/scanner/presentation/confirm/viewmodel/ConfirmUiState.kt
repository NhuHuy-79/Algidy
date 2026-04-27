package com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.FoodItem

@Immutable
data class ConfirmUiState(
    val foodItem: FoodItem = FoodItem(),
    val expandedUnitMenu: Boolean = false,
    val overlay: ConfirmOverlay = ConfirmOverlay.NONE,
)

enum class ConfirmOverlay {
    NONE, EXPIRY_DATE_PICKER, PURCHASE_DATE_PICKER, ERROR_DIALOG, EXIT_DIALOG
}