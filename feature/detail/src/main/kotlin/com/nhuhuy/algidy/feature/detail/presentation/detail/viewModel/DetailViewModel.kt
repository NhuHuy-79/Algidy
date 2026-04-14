package com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.FoodValidator
import com.nhuhuy.algidy.core.model.StorageLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val foodItemId: String,
    private val foodRepository: FoodRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _editEntry = MutableStateFlow(EditEntryUiState())
    val editEntry = _editEntry.asStateFlow()

    private val _errorState = MutableStateFlow(EditEntryError())
    val errorState = _errorState.asStateFlow()

    init {
        viewModelScope.launch {

        }
    }

    fun onAction(action: DetailAction) {
        when (action) {
            DetailAction.OnWastedItem -> {

                updateActionState(DetailActionState.Wasted)
            }

            DetailAction.OnConsumeItem -> {
                updateActionState(DetailActionState.Consume)
            }

            DetailAction.OnDismiss -> {
                updateActionState(DetailActionState.None)
            }

            DetailAction.OnEditItem -> {
                val item = _uiState.value.foodItem
                updateActionState(DetailActionState.Edit)
                _editEntry.update { entry ->
                    entry.copy(
                        name = item.name,
                        imageUri = item.imageUri,
                        location = item.location,
                        quantity = item.quantity,
                        itemUnit = item.itemUnit,
                        expiryDate = item.expiryDate,
                        purchaseDate = item.purchaseDate,
                        isFavorite = item.isFavorite,
                        notes = item.notes
                    )
                }
            }

            is DetailAction.EditEntryAction.OnExpiryDateChange -> onExpiryDateChange(action.expiryDate)
            is DetailAction.EditEntryAction.OnNameChange -> onNameChange(action.name)
            is DetailAction.EditEntryAction.OnQuantityChange -> onQuantityChange(action.quantity)
            DetailAction.EditEntryAction.OnSave -> {

            }

            is DetailAction.EditEntryAction.OnStorageLocationChange -> onLocationChange(action.location)
            is DetailAction.EditEntryAction.OnNoteChange -> onNoteChange(action.note)
        }
    }

    fun onNameChange(name: String) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(name = name)
        }
        _errorState.update { error ->
            error.copy(nameError = FoodValidator.validateName(name))
        }
    }

    fun onQuantityChange(quantity: Double) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(quantity = quantity)
        }
        _errorState.update { error ->
            error.copy(quantityError = FoodValidator.validateQuantity(quantity))
        }
    }

    fun onLocationChange(location: StorageLocation) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(location = location)
        }
    }

    fun onNoteChange(note: String) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(notes = note)
        }
    }

    fun onExpiryDateChange(expiryDate: Long) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(expiryDate = expiryDate)
        }
        _errorState.update { error ->
            error.copy(expiryDateError = FoodValidator.validateExpiryDate(expiryDate))
        }
    }

    fun onUpdateFoodItem(newItem: FoodItem) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(foodItem = newItem)
            }
            //Update new Item
        }
    }

    fun updateActionState(actionState: DetailActionState) {
        _uiState.update { state -> state.copy(actionState = actionState) }
    }
}