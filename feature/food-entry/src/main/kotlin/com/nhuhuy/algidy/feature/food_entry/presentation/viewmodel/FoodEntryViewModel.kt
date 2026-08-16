package com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.model.food.FoodCategory
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.validate.FoodValidator
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.core.presentation.viewmodel.UiEvent
import com.nhuhuy.algidy.feature.food_entry.domain.model.FoodEntryPreferences
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.AddCategoryUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.FoodEntryPreferencesUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.GetFoodByIdUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.ObserveCategoriesUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.SaveFoodItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class FoodEntryViewModel(
    foodId: String?,
    observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val getFoodByIdUseCase: GetFoodByIdUseCase,
    private val foodEntryPreferencesUseCase: FoodEntryPreferencesUseCase,
    private val saveFoodItemUseCase: SaveFoodItemUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val navigator: Navigator,
) : BaseViewModel<FoodEntryUiState, FoodEntryEvent, FoodEntryAction>() {

    private val _uiState = MutableStateFlow(FoodEntryUiState())
    override val uiState: StateFlow<FoodEntryUiState> = _uiState.asStateFlow()

    private val _entryError = MutableStateFlow(FoodEntryError())
    val entryError = _entryError.asStateFlow()

    val foodEntryPreferences = foodEntryPreferencesUseCase.observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), FoodEntryPreferences())
    private val currentPreferences get() = foodEntryPreferences.value

    init {
        viewModelScope.launch {
            val foodItem = foodId?.let { getFoodByIdUseCase(it) }
            foodItem?.let { setEntryData(it) }
            val category = foodItem?.categoryId?.let {
                addCategoryUseCase.getCurrentCategory(it)
            }

            val categoryUiModel =
                category?.let { CategoryUiModel.ByCategory(it) } ?: CategoryUiModel.All
            _uiState.product { copy(currentCategory = categoryUiModel) }
        }

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
            FoodEntryAction.OnBackClick -> navigator.navigateBack()
            is FoodEntryAction.OnNotificationGranted -> viewModelScope.launch {
                if (!currentPreferences.hasAskNotificationPermission) {
                    foodEntryPreferencesUseCase.askNotificationPermission(true)
                }
            }
        }
    }

    private fun saveFood() {
        if (!_entryError.value.isValid) return
        if (currentPreferences.addItemFirst && !currentPreferences.hasAskNotificationPermission) {
            emitEvent(FoodEntryEvent.AskNotificationPermission)
            return
        }

        performSave()
    }

    private fun performSave() {
        viewModelScope.launch {
            val foodItem = getResultFoodItem()
            saveFoodItemUseCase(foodItem)

            if (!currentPreferences.addItemFirst) {
                foodEntryPreferencesUseCase.addItemFirst(true)
            }

            navigator.navigateBack()
        }
    }

    private fun setEntryData(foodItem: FoodItem) {
        _uiState.update {
            it.copy(
                id = foodItem.id,
                name = foodItem.name,
                categoryId = foodItem.categoryId ?: "",
                location = foodItem.location,
                purchaseDate = foodItem.purchaseDate,
                expiryDate = foodItem.expiryDate,
                imageUri = foodItem.imageUri,
                notes = foodItem.note
            )
        }
        _entryError.update {
            FoodEntryError(
                nameValidation = FoodValidator.validateName(foodItem.name),
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
            location = state.location,
            purchaseDate = state.purchaseDate,
            expiryDate = state.expiryDate,
            imageUri = state.imageUri,
            note = state.notes
        )
    }
}

sealed interface FoodEntryEvent : UiEvent {
    data object AskNotificationPermission : FoodEntryEvent
}

