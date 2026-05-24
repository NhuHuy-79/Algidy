package com.nhuhuy.algidy.core.presentation.delegate

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.validate.FoodValidator
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryError
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Delegate to handle shared logic for food entry forms.
 * Follows UDF pattern: Action -> Delegate -> State.
 */
interface FoodEntryDelegate {
    val entryState: StateFlow<FoodEntryUiState>
    val entryError: StateFlow<FoodEntryError>

    fun onEntryAction(action: FoodEntryAction)
    fun setEntryData(foodItem: FoodItem)
    fun setCurrentCategory(category: CategoryUiModel.ByCategory)
    fun updateCategories(categories: List<CategoryUiModel.ByCategory>)
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
                _entryState.update { it.updateExpiryDate(action.expiryDate) }
            }

            is FoodEntryAction.OnNoteChange -> {
                _entryState.update { it.copy(notes = action.note) }
            }

            is FoodEntryAction.OnImagePick -> {
                _entryState.update { it.copy(imageUri = action.uri.toString()) }
            }

            is FoodEntryAction.OnCategoryQueryChange -> {
                _entryState.update { it.copy(categoryQuery = action.query) }
            }

            is FoodEntryAction.OnCategorySelect -> {
                _entryState.update {
                    it.copy(
                        currentCategory = action.category,
                        categoryId = action.category.data.id,
                        categoryQuery = action.category.data.name
                    )
                }
            }

            is FoodEntryAction.OnCategorySelectById -> {
                val category = _entryState.value.categories.find { it.data.id == action.id }
                category?.let { cat ->
                    _entryState.update {
                        it.copy(
                            categoryId = cat.data.id,
                            categoryQuery = cat.data.name,
                            currentCategory = cat
                        )
                    }
                }
            }

            FoodEntryAction.OnCategoryConfirm -> {
                // To be handled by consumer (ViewModel) to perform DB operation.
                // The delegate just updates state if needed or acts as a relay.
            }
        }
    }

    private fun FoodEntryUiState.updateExpiryDate(newExpiryDate: Long): FoodEntryUiState {
        _entryError.update {
            it.copy(
                expiryDateValidation = FoodValidator.validateExpiryDate(
                    purchaseDate = this.purchaseDate,
                    expiryDate = newExpiryDate
                )
            )
        }
        return copy(expiryDate = newExpiryDate)
    }

    override fun setEntryData(foodItem: FoodItem) {
        val category = _entryState.value.categories.find { it.data.id == foodItem.categoryId }
        _entryState.update {
            it.copy(
                id = foodItem.id,
                name = foodItem.name,
                categoryId = foodItem.categoryId,
                categoryQuery = category?.data?.name ?: "",
                currentCategory = category ?: CategoryUiModel.All,
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

    override fun setCurrentCategory(category: CategoryUiModel.ByCategory) {
        _entryState.update {
            it.copy(
                currentCategory = category,
                categoryId = category.data.id,
                categoryQuery = category.data.name
            )
        }
    }

    override fun updateCategories(categories: List<CategoryUiModel.ByCategory>) {
        val currentCategoryId = _entryState.value.categoryId
        val selectedCategory = categories.find { it.data.id == currentCategoryId }
        _entryState.update { it.copy(
            categories = categories,
            categoryQuery = selectedCategory?.data?.name ?: it.categoryQuery,
            currentCategory = selectedCategory ?: it.currentCategory
        ) }
    }

    override fun resetEntryData() {
        val currentCategories = _entryState.value.categories
        _entryState.update { FoodEntryUiState(categories = currentCategories) }
        _entryError.update { FoodEntryError() }
    }

    override fun getResultFoodItem(): FoodItem {
        val state = _entryState.value
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
