package com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.model.validate.FoodValidator
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.detail.domain.usecase.GetFoodDetailUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.MarkFoodAsConsumedUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.MarkFoodAsWastedUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.UpdateFoodDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val foodItemId: String,
    private val getFoodDetailUseCase: GetFoodDetailUseCase,
    private val markFoodAsConsumedUseCase: MarkFoodAsConsumedUseCase,
    private val markFoodAsWastedUseCase: MarkFoodAsWastedUseCase,
    private val updateFoodDetailUseCase: UpdateFoodDetailUseCase,
) : BaseViewModel<DetailUiState, DetailEvent, DetailAction>() {
    private val _uiState = MutableStateFlow(DetailUiState())
    override val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()
    private val stateValue get() = uiState.value
    private val _editEntry = MutableStateFlow(EditEntryUiState())
    val editEntry = _editEntry.asStateFlow()

    private val _errorState = MutableStateFlow(EditEntryError())
    val errorState = _errorState.asStateFlow()

    init {
        viewModelScope.launch {
            val foodItem = getFoodDetailUseCase(foodId = foodItemId) ?: FoodItem()
            _uiState.product { copy(detailFoodItem = foodItem) }
        }
    }

    override fun onAction(action: DetailAction) {
        when (action) {
            DetailAction.OnWastedItem -> wasteFoodItem()
            DetailAction.OnConsumeItem -> consumeFoodItem()
            DetailAction.OnDismiss -> updateActionState(DetailOverlay.None)
            DetailAction.OnEditItem -> updateEditEntry()
            is DetailAction.EditEntryAction.OnExpiryDateChange -> onExpiryDateChange(action.expiryDate)
            is DetailAction.EditEntryAction.OnNameChange -> onNameChange(action.name)
            is DetailAction.EditEntryAction.OnQuantityChange -> onQuantityChange(action.quantity)
            is DetailAction.EditEntryAction.OnStorageLocationChange -> onLocationChange(action.location)
            is DetailAction.EditEntryAction.OnNoteChange -> onNoteChange(action.note)
            is DetailAction.EditEntryAction.OnItemUnitChange -> onItemUnitChange(action.unit)
            is DetailAction.EditEntryAction.OnPurchaseDateChange -> onPurchaseDateChange(action.purchaseDate)
            DetailAction.EditEntryAction.OnSave -> onUpdateFoodItem()
            is DetailAction.EditEntryAction.OnImageChange -> action.uri?.let { onImageChange(action.uri) }
            DetailAction.OnConsumeFabPress -> updateActionState(DetailOverlay.Wasted)
            DetailAction.OnWasteFabPress -> updateActionState(DetailOverlay.Consume)
        }
    }

    private fun updateEditEntry() {
        val item = _uiState.value.detailFoodItem
        updateActionState(DetailOverlay.Edit)
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
            updateFoodDetailUseCase(
                newItem = stateValue.detailFoodItem,
                newImageUri = uri.toString()
            )
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
        if (!_errorState.value.valid) return

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
            updateActionState(DetailOverlay.None)
            updateFoodDetailUseCase(
                newItem = newFoodItem,
                newImageUri = null
            )
        }
    }

    private fun updateActionState(actionState: DetailOverlay) {
        _uiState.update { state -> state.copy(actionState = actionState) }
    }

    private fun consumeFoodItem() {
        viewModelScope.launch {
            _uiState.product { copy(actionState = DetailOverlay.None) }
            markFoodAsConsumedUseCase(foodId = foodItemId)
        }
    }

    private fun wasteFoodItem() {
        viewModelScope.launch {
            _uiState.product { copy(actionState = DetailOverlay.None) }
            markFoodAsWastedUseCase(foodId = foodItemId)
        }
    }
}
