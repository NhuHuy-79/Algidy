package com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel

import android.net.Uri
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation

sealed interface ConfirmAction {
    data object OnDismissRequest : ConfirmAction

    data class OnImageChange(val uri: Uri?) : ConfirmAction
    data class OnNameChange(val name: String) : ConfirmAction
    data class OnQuantityChange(val quantity: String) : ConfirmAction
    data class OnUnitSelected(val unit: ItemUnit) : ConfirmAction
    data class OnLocationChange(val location: StorageLocation) : ConfirmAction
    data class OnNotesChange(val notes: String) : ConfirmAction

    data class OnToggleUnitMenu(val isOpen: Boolean) : ConfirmAction
    data class OnTogglePurchaseDatePicker(val isOpen: Boolean) : ConfirmAction
    data class OnToggleExpiryDatePicker(val isOpen: Boolean) : ConfirmAction

    data class OnPurchaseDateChange(val timestamp: Long) : ConfirmAction
    data class OnExpiryDateChange(val timestamp: Long) : ConfirmAction

    data object OnExitAlertDialog : ConfirmAction
    data object OnSaveClick : ConfirmAction
}
