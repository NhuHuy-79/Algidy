package com.nhuhuy.algidy.core.presentation.delegate

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.validate.FoodValidator
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryError
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface FoodEntryDelegate {
    val entryState: StateFlow<FoodEntryUiState>
    val entryError: StateFlow<FoodEntryError>

    fun onEntryAction(action: FoodEntryAction)
    fun setEntryData(foodItem: FoodItem)
    fun resetEntryData()
    fun getResultFoodItem(): FoodItem
}

class FoodEntryDelegateImpl : FoodEntryDelegate {
    private val _entryState = MutableStateFlow(FoodEntryUiState())
    override val entryState = _entryState.asStateFlow()

    private val _entryError = MutableStateFlow(FoodEntryError())
    override val entryError = _entryError.asStateFlow()

    override fun onEntryAction(action: FoodEntryAction) {
        when (action) {
            is FoodEntryAction.OnNameChange -> {
                _entryState.update { it.copy(name = action.name) }
                _entryError.update { it.copy(nameValidation = FoodValidator.validateName(action.name)) }
            }

            is FoodEntryAction.OnQuantityChange -> {
                _entryState.update { it.copy(quantity = action.quantity) }
                _entryError.update {
                    it.copy(
                        quantityValidation = FoodValidator.validateQuantity(
                            action.quantity
                        )
                    )
                }
            }

            is FoodEntryAction.OnItemUnitChange -> {
                _entryState.update { it.copy(itemUnit = action.unit) }
            }

            is FoodEntryAction.OnStorageLocationChange -> {
                _entryState.update { it.copy(location = action.location) }
            }

            is FoodEntryAction.OnPurchaseDateChange -> {
                _entryState.update { it.copy(purchaseDate = action.purchaseDate) }
                _entryError.update {
                    it.copy(purchaseDateValidation = FoodValidator.validatePurchaseDate(action.purchaseDate))
                }
            }

            is FoodEntryAction.OnExpiryDateChange -> {
                _entryState.update { it.copy(expiryDate = action.expiryDate) }
                _entryError.update {
                    it.copy(
                        expiryDateValidation = FoodValidator.validateExpiryDate(
                            purchaseDate = _entryState.value.purchaseDate,
                            expiryDate = action.expiryDate
                        )
                    )
                }
            }

            is FoodEntryAction.OnNoteChange -> {
                _entryState.update { it.copy(notes = action.note) }
            }

            is FoodEntryAction.OnImagePick -> {
                _entryState.update { it.copy(imageUri = action.uri.toString()) }
            }
        }
    }

    override fun setEntryData(foodItem: FoodItem) {
        _entryState.update {
            it.copy(
                id = foodItem.id,
                name = foodItem.name,
                categoryId = foodItem.categoryId,
                foodCategory = foodItem.foodCategory,
                location = foodItem.location,
                quantity = foodItem.quantity,
                itemUnit = foodItem.itemUnit,
                purchaseDate = foodItem.purchaseDate,
                expiryDate = foodItem.expiryDate,
                imageUri = foodItem.imageUri,
                isFavorite = foodItem.isFavorite,
                notes = foodItem.notes
            )
        }
        _entryError.update {
            FoodEntryError(
                nameValidation = FoodValidator.validateName(foodItem.name),
                quantityValidation = FoodValidator.validateQuantity(foodItem.quantity),
                purchaseDateValidation = FoodValidator.validatePurchaseDate(foodItem.purchaseDate),
                expiryDateValidation = FoodValidator.validateExpiryDate(
                    purchaseDate = foodItem.purchaseDate,
                    expiryDate = foodItem.expiryDate
                )
            )
        }
    }

    override fun resetEntryData() {
        _entryState.update { FoodEntryUiState() }
        _entryError.update { FoodEntryError() }
    }

    override fun getResultFoodItem(): FoodItem {
        val state = _entryState.value
        return FoodItem(
            id = state.id.ifBlank { java.util.UUID.randomUUID().toString() },
            name = state.name,
            categoryId = state.categoryId,
            foodCategory = state.foodCategory,
            location = state.location,
            quantity = state.quantity,
            itemUnit = state.itemUnit,
            purchaseDate = state.purchaseDate,
            expiryDate = state.expiryDate,
            imageUri = state.imageUri,
            isFavorite = state.isFavorite,
            notes = state.notes
        )
    }
}
