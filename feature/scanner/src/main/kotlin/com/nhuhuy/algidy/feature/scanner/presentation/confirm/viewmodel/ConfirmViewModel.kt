package com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.LocalMediaStorage
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.FoodValidator
import com.nhuhuy.algidy.core.model.ItemUnit
import com.nhuhuy.algidy.core.model.Resource
import com.nhuhuy.algidy.core.model.StorageLocation
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfirmViewModel(
    private val foodId: String,
    private val foodRepository: FoodRepository,
    private val localMediaStorage: LocalMediaStorage
) : ViewModel() {
    private val _confirmEvent = Channel<ConfirmEvent>(onBufferOverflow = BufferOverflow.SUSPEND)
    val confirmEvent = _confirmEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(ConfirmUiState())
    val uiState: StateFlow<ConfirmUiState> = _uiState.asStateFlow()

    private val stateValue get() = uiState.value

    init {
        viewModelScope.launch {
            val foodItem = foodRepository.getFoodById(foodId) ?: FoodItem()
            _uiState.update {
                it.copy(
                    foodItem = foodItem,
                    errorState = ConfirmError(
                        nameValidation = FoodValidator.validateName(foodItem.name),
                        quantityValidation = FoodValidator.validateQuantity(foodItem.quantity),
                        purchaseDateValidation = FoodValidator.validatePurchaseDate(foodItem.purchaseDate),
                        expiryDateValidation = FoodValidator.validateExpiryDate(
                            foodItem.expiryDate,
                            foodItem.purchaseDate
                        )
                    )
                )
            }
            foodRepository.removeFoodItem(id = foodItem.id)
        }
    }

    fun onAction(action: ConfirmAction) {
        when (action) {
            is ConfirmAction.OnNameChange -> onNameChange(action.name)
            is ConfirmAction.OnQuantityChange -> onQuantityChange(action.quantity)
            is ConfirmAction.OnUnitSelected -> onUnitSelected(action.unit)
            is ConfirmAction.OnLocationChange -> onLocationChange(action.location)
            is ConfirmAction.OnNotesChange -> onNotesChange(action.notes)
            is ConfirmAction.OnPurchaseDateChange -> onPurchaseDateChange(action.timestamp)
            is ConfirmAction.OnExpiryDateChange -> onExpiryDateChange(action.timestamp)
            is ConfirmAction.OnToggleUnitMenu -> _uiState.update { it.copy(expandedUnitMenu = action.isOpen) }
            is ConfirmAction.OnTogglePurchaseDatePicker -> _uiState.update { it.copy(overlay = ConfirmOverlay.PURCHASE_DATE_PICKER) }
            is ConfirmAction.OnToggleExpiryDatePicker -> _uiState.update { it.copy(overlay = ConfirmOverlay.EXPIRY_DATE_PICKER) }
            ConfirmAction.OnSaveClick -> onSave()
            ConfirmAction.OnDismissRequest -> _uiState.update { it.copy(overlay = ConfirmOverlay.NONE) }
            ConfirmAction.OnExitAlertDialog -> _uiState.update { it.copy(overlay = ConfirmOverlay.EXIT_DIALOG) }
            is ConfirmAction.OnImageChange -> onImageChange(action.uri)
        }
    }

    private fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                foodItem = it.foodItem.copy(name = name),
                errorState = it.errorState.copy(nameValidation = FoodValidator.validateName(name))
            )
        }
    }

    private fun onQuantityChange(quantityStr: String) {
        val quantity = quantityStr.toDoubleOrNull() ?: 0.0
        _uiState.update {
            it.copy(
                foodItem = it.foodItem.copy(quantity = quantity),
                errorState = it.errorState.copy(
                    quantityValidation = FoodValidator.validateQuantity(
                        quantity
                    )
                )
            )
        }
    }

    private fun onUnitSelected(unit: ItemUnit) {
        _uiState.update {
            it.copy(
                foodItem = it.foodItem.copy(itemUnit = unit),
                expandedUnitMenu = false
            )
        }
    }

    private fun onLocationChange(location: StorageLocation) {
        _uiState.update {
            it.copy(foodItem = it.foodItem.copy(location = location))
        }
    }

    private fun onNotesChange(notes: String) {
        _uiState.update {
            it.copy(foodItem = it.foodItem.copy(notes = notes))
        }
    }

    private fun onPurchaseDateChange(timestamp: Long) {
        _uiState.update {
            it.copy(
                foodItem = it.foodItem.copy(purchaseDate = timestamp),
                errorState = it.errorState.copy(
                    purchaseDateValidation = FoodValidator.validatePurchaseDate(timestamp),
                    expiryDateValidation = FoodValidator.validateExpiryDate(
                        it.foodItem.expiryDate,
                        timestamp
                    )
                )
            )
        }
    }

    private fun onExpiryDateChange(timestamp: Long) {
        _uiState.update {
            it.copy(
                foodItem = it.foodItem.copy(expiryDate = timestamp),
                errorState = it.errorState.copy(
                    expiryDateValidation = FoodValidator.validateExpiryDate(
                        timestamp,
                        it.foodItem.purchaseDate
                    )
                )
            )
        }
    }

    private fun onImageChange(uri: Uri?) {
        uri?.let { uri ->
            _uiState.update {
                it.copy(foodItem = it.foodItem.copy(imageUri = uri.toString()))
            }
        }
    }

    private fun onSave() {
        if (stateValue.errorState.valid) {
            viewModelScope.launch {
                val imageUri = stateValue.foodItem.imageUri
                val finalImageUri = if (imageUri != null && imageUri.startsWith("content://")) {
                    when (val result = localMediaStorage.copyImageToInternalStorage(imageUri)) {
                        is Resource.Success -> result.data
                        else -> imageUri
                    }
                } else {
                    imageUri
                }

                val finalFoodItem = stateValue.foodItem.copy(imageUri = finalImageUri)
                foodRepository.addFoodItem(finalFoodItem)
                _confirmEvent.trySend(ConfirmEvent.OnSaveSuccessfully)
            }
        }
    }
}
