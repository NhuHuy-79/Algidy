package com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.model.food.FoodCategory
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.validate.FoodValidator
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryError
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryOverlay
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryUiState
import com.nhuhuy.algidy.core.presentation.viewmodel.UiEvent
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.AddCategoryUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.ObserveCategoriesUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.SaveFoodItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FoodEntryViewModel(
    initialFoodItem: FoodItem?,
    observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val saveFoodItemUseCase: SaveFoodItemUseCase,
    private val addCategoryUseCase: AddCategoryUseCase
) : BaseViewModel<FoodEntryUiState, FoodEntryEvent, FoodEntryAction>() {

    private val _uiState = MutableStateFlow(FoodEntryUiState())
    override val uiState: StateFlow<FoodEntryUiState> = _uiState.asStateFlow()

    private val _entryError = MutableStateFlow(FoodEntryError())
    val entryError = _entryError.asStateFlow()

    init {
        initialFoodItem?.let { setEntryData(it) }

        observeCategoriesUseCase().onEach { categories ->
            _uiState.update {
                it.copy(categories = categories.map { category ->
                    CategoryUiModel.ByCategory(category)
                })
            }
        }.launchIn(viewModelScope)
    }

    override fun onAction(action: FoodEntryAction) {
        when (action) {
            is FoodEntryAction.OnNameChange -> {
                _uiState.update { it.copy(name = action.name) }
                _entryError.update { it.copy(nameValidation = FoodValidator.validateName(action.name)) }
            }

            is FoodEntryAction.OnQuantityChange -> {
                _uiState.update { it.copy(quantity = action.quantity) }
                _entryError.update {
                    it.copy(
                        quantityValidation = FoodValidator.validateQuantity(
                            action.quantity
                        )
                    )
                }
            }

            is FoodEntryAction.OnItemUnitChange -> _uiState.update { it.copy(itemUnit = action.unit) }
            is FoodEntryAction.OnStorageLocationChange -> _uiState.update { it.copy(location = action.location) }
            is FoodEntryAction.OnPurchaseDateChange -> {
                _uiState.update {
                    it.copy(
                        purchaseDate = action.purchaseDate,
                        overlay = FoodEntryOverlay.NONE
                    )
                }
                _entryError.update {
                    it.copy(
                        purchaseDateValidation = FoodValidator.validatePurchaseDate(
                            action.purchaseDate
                        )
                    )
                }
            }

            is FoodEntryAction.OnExpiryDateChange -> {
                _uiState.update {
                    it.copy(
                        expiryDate = action.expiryDate,
                        overlay = FoodEntryOverlay.NONE
                    )
                }
                _entryError.update {
                    it.copy(
                        expiryDateValidation = FoodValidator.validateExpiryDate(
                            purchaseDate = _uiState.value.purchaseDate,
                            expiryDate = action.expiryDate
                        )
                    )
                }

            }

            is FoodEntryAction.OnNoteChange -> _uiState.update { it.copy(notes = action.note) }
            is FoodEntryAction.OnImagePick -> _uiState.update { it.copy(imageUri = action.uri.toString()) }

            is FoodEntryAction.OnCategoryQueryChange -> _uiState.update { it.copy(categoryQuery = action.query) }
            is FoodEntryAction.OnCategorySelect -> _uiState.update {
                it.copy(currentCategory = action.category, categoryId = action.category.data.id)
            }

            is FoodEntryAction.OnCategorySelectById -> {
                val category = _uiState.value.categories.find { it.data.id == action.id }
                category?.let {
                    _uiState.update { it.copy(currentCategory = category, categoryId = action.id) }
                }
                _uiState.update {
                    it.copy(overlay = FoodEntryOverlay.NONE)
                }
            }

            FoodEntryAction.OnCategoryConfirm -> {
                viewModelScope.launch {
                    addCategoryUseCase(
                        category = FoodCategory(
                            name = _uiState.value.categoryQuery
                        )
                    )
                    _uiState.update {
                        it.copy(overlay = FoodEntryOverlay.NONE)
                    }
                }
            }

            is FoodEntryAction.OnShowOverlay -> _uiState.update { it.copy(overlay = action.overlay) }
            FoodEntryAction.OnDismissOverlay -> _uiState.update { it.copy(overlay = FoodEntryOverlay.NONE) }
            FoodEntryAction.OnSaveClick -> saveFood()
            FoodEntryAction.OnBackClick -> emitEvent(FoodEntryEvent.NavigateBack)
        }
    }

    private fun saveFood() {
        if (_entryError.value.isValid) {
            viewModelScope.launch {
                val foodItem = getResultFoodItem()
                saveFoodItemUseCase(foodItem)
                emitEvent(FoodEntryEvent.OnSaveSuccess)
            }
        }
    }

    private fun setEntryData(foodItem: FoodItem) {
        _uiState.update {
            it.copy(
                id = foodItem.id,
                name = foodItem.name,
                categoryId = foodItem.categoryId,
                defaultFoodCategory = foodItem.defaultFoodCategory,
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

    private fun getResultFoodItem(): FoodItem {
        val state = _uiState.value
        return FoodItem(
            id = state.id.ifBlank { java.util.UUID.randomUUID().toString() },
            name = state.name,
            categoryId = state.categoryId,
            defaultFoodCategory = state.defaultFoodCategory,
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

sealed interface FoodEntryEvent : UiEvent {
    data object OnSaveSuccess : FoodEntryEvent
    data object NavigateBack : FoodEntryEvent
}

