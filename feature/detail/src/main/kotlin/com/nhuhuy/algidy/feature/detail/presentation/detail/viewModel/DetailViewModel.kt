package com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.FoodValidator
import com.nhuhuy.algidy.core.model.ItemUnit
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
    private val stateValue get() = uiState.value
    private val _editEntry = MutableStateFlow(EditEntryUiState())
    val editEntry = _editEntry.asStateFlow()

    private val _errorState = MutableStateFlow(EditEntryError())
    val errorState = _errorState.asStateFlow()

    init {
        viewModelScope.launch {
            val foodItem = foodRepository.getFoodById(id = foodItemId) ?: FoodItem()
            _uiState.product { copy(detailFoodItem = foodItem) }
        }
    }

    fun onAction(action: DetailAction) {
        when (action) {
            DetailAction.OnWastedItem -> updateActionState(DetailActionState.Wasted)
            DetailAction.OnConsumeItem -> updateActionState(DetailActionState.Consume)
            DetailAction.OnDismiss -> updateActionState(DetailActionState.None)
            DetailAction.OnEditItem -> {
                val item = _uiState.value.detailFoodItem
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
            is DetailAction.EditEntryAction.OnStorageLocationChange -> onLocationChange(action.location)
            is DetailAction.EditEntryAction.OnNoteChange -> onNoteChange(action.note)
            is DetailAction.EditEntryAction.OnItemUnitChange -> onItemUnitChange(action.unit)
            is DetailAction.EditEntryAction.OnPurchaseDateChange -> onPurchaseDateChange(action.purchaseDate)
            DetailAction.EditEntryAction.OnSave -> onUpdateFoodItem()
            is DetailAction.EditEntryAction.OnImageChange -> action.uri?.let {
                onImageChange(action.uri)
            }
        }
    }


    private fun onNameChange(name: String) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(name = name)
        }
        _errorState.update { error ->
            error.copy(nameValidation = FoodValidator.validateName(name))
        }
    }

    private fun onImageChange(uri: Uri) {
        viewModelScope.launch {
            val newFoodItem = stateValue.detailFoodItem.copy(imageUri = uri.toString())
            _uiState.product { copy(detailFoodItem = newFoodItem) }
            foodRepository.addFoodItem(newFoodItem)
        }
    }

    private fun onItemUnitChange(unit: ItemUnit) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(itemUnit = unit)
        }
    }

    private fun onQuantityChange(quantity: Double) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(quantity = quantity)
        }
        _errorState.update { error ->
            error.copy(quantityValidation = FoodValidator.validateQuantity(quantity))
        }
    }

    private fun onLocationChange(location: StorageLocation) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(location = location)
        }
    }

    private fun onNoteChange(note: String) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(notes = note)
        }
    }

    private fun onPurchaseDateChange(purchaseDate: Long) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(purchaseDate = purchaseDate)
        }
        _errorState.product {
            copy(
                purchaseDateValidation = FoodValidator.validatePurchaseDate(
                    purchaseDate = purchaseDate,
                )
            )
        }
    }

    private fun onExpiryDateChange(expiryDate: Long) {
        _editEntry.update { entryUiState ->
            entryUiState.copy(expiryDate = expiryDate)
        }
        _errorState.update { error ->
            error.copy(
                expiryDateValidation = FoodValidator.validateExpiryDate(
                    purchaseDate = _editEntry.value.purchaseDate,
                    expiryDate = expiryDate
                )
            )
        }
    }

    private fun onUpdateFoodItem() {
        viewModelScope.launch {
            val oldFoodItem: FoodItem = stateValue.detailFoodItem
            val newFoodItem = oldFoodItem.copy(
                name = _editEntry.value.name,
                imageUri = _editEntry.value.imageUri,
                location = _editEntry.value.location,
                quantity = _editEntry.value.quantity,
                itemUnit = _editEntry.value.itemUnit,
                expiryDate = _editEntry.value.expiryDate,
                purchaseDate = _editEntry.value.purchaseDate,
                isFavorite = _editEntry.value.isFavorite,
                notes = _editEntry.value.notes
            )
            _uiState.product { copy(detailFoodItem = newFoodItem) }
            updateActionState(DetailActionState.None)
            foodRepository.addFoodItem(item = newFoodItem)
        }
    }
    private fun updateActionState(actionState: DetailActionState) {
        _uiState.update { state -> state.copy(actionState = actionState) }
    }
}