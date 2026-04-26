package com.nhuhuy.algidy.feature.confirm.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ConfirmViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(ConfirmUiState())
    val uiState: StateFlow<ConfirmUiState> = _uiState.asStateFlow()

    private val stateValue get() = uiState.value

    fun onAction(action: ConfirmAction) {
        when (action) {
            is ConfirmAction.OnNameChange -> _uiState.update {
                it.copy(foodItem = it.foodItem.copy(name = action.name))
            }

            is ConfirmAction.OnQuantityChange -> _uiState.update {
                it.copy(
                    foodItem = it.foodItem.copy(
                        quantity = action.quantity.toDoubleOrNull() ?: 0.0
                    )
                )
            }

            is ConfirmAction.OnUnitSelected -> _uiState.update {
                it.copy(
                    foodItem = it.foodItem.copy(itemUnit = action.unit),
                    expandedUnitMenu = false
                )
            }

            is ConfirmAction.OnLocationChange -> _uiState.update {
                it.copy(foodItem = it.foodItem.copy(location = action.location))
            }

            is ConfirmAction.OnNotesChange -> _uiState.update {
                it.copy(foodItem = it.foodItem.copy(notes = action.notes))
            }

            is ConfirmAction.OnPurchaseDateChange -> _uiState.update {
                it.copy(foodItem = it.foodItem.copy(purchaseDate = action.timestamp))
            }

            is ConfirmAction.OnExpiryDateChange -> _uiState.update {
                it.copy(foodItem = it.foodItem.copy(expiryDate = action.timestamp))
            }

            // --- Điều khiển Trạng thái UI (Toggles) ---
            is ConfirmAction.OnToggleUnitMenu -> _uiState.update {
                it.copy(expandedUnitMenu = action.isOpen)
            }

            is ConfirmAction.OnTogglePurchaseDatePicker -> _uiState.update {
                it.copy(overlay = ConfirmOverlay.PURCHASE_DATE_PICKER)
            }

            is ConfirmAction.OnToggleExpiryDatePicker -> _uiState.update {
                it.copy(overlay = ConfirmOverlay.EXPIRY_DATE_PICKER)
            }

            ConfirmAction.OnSaveClick -> {

            }

            ConfirmAction.OnDismissRequest -> _uiState.update {
                it.copy(overlay = ConfirmOverlay.NONE)
            }
        }
    }
}