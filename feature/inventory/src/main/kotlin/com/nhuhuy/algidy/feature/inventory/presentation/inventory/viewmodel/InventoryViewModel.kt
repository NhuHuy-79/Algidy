package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.delegate.FoodEntryDelegate
import com.nhuhuy.algidy.core.presentation.delegate.FoodEntryDelegateImpl
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.*
import com.nhuhuy.algidy.feature.inventory.domain.usecase.AddCategoryUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.CreateFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.DeleteFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.ObserveCategoriesUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.ObserveFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.ObserveSettingDataUseCase
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(
    observerFoodItemUseCase: ObserveFoodItemUseCase,
    private val foodEntryDelegateImpl: FoodEntryDelegateImpl,
    private val createFoodItemUseCase: CreateFoodItemUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteFoodItemUseCase: DeleteFoodItemUseCase,
    observeSettingDataUseCase: ObserveSettingDataUseCase,
    observeCategoriesUseCase: ObserveCategoriesUseCase
) : BaseViewModel<InventoryUiState, InventoryEvent, InventoryAction>(),
    FoodEntryDelegate by foodEntryDelegateImpl {
    private val _uiState = MutableStateFlow(InventoryUiState())
    override val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    val combineState: StateFlow<InventoryCombineState> = combine(
        observeSettingDataUseCase.getCategoryEnabled(),
        observeCategoriesUseCase()
    ) { categoryEnabled, categories ->
        InventoryCombineState(
            categoryEnabled = categoryEnabled,
            categories = categories.toUiModel()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InventoryCombineState()
    )

    init {
        resetEntryData()
    }

    val resultState: StateFlow<InventoryResultState> = observerFoodItemUseCase()
        .map { items ->
            if (items.isEmpty()) InventoryResultState.Empty
            else InventoryResultState.Success(items = items)
        }
        .onStart { emit(InventoryResultState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InventoryResultState.Loading
        )

    override fun onAction(action: InventoryAction) {
        when (action) {
            is InventoryAction.RemoveItem -> {
                viewModelScope.launch {
                    deleteFoodItemUseCase(id = action.id)
                }
            }

            InventoryAction.OnAddFabClick -> _uiState.product {
                copy(overlay = InventoryOverlay.FOOD_SHEET)
            }

            InventoryAction.OnDismiss -> _uiState.product { copy(overlay = InventoryOverlay.NONE) }
            InventoryAction.OnManuallyClick -> viewModelScope.launch {
                val foodEntry: FoodItem = getResultFoodItem()
                _uiState.product { copy(overlay = InventoryOverlay.NONE) }
                resetEntryData()
                createFoodItemUseCase(foodItem = foodEntry)
            }

            is InventoryAction.ToggleFabMenu -> {
                _uiState.product { copy(expanded = action.value) }
            }

            is InventoryAction.OnCategorySelect -> _uiState.product {
                copy(currentCategory = action.categoryUiModel)
            }

            is InventoryAction.OnCreateCategory -> {
                viewModelScope.launch {
                    val newCategory = addCategoryUseCase(action.name)
                    onEntryAction(OnCategoryChange(newCategory.id))
                }
            }

            is InventoryAction.OnEditCategorySheet.OnInputChange -> TODO()
            InventoryAction.OnEditCategorySheet.Open -> TODO()
            InventoryAction.OnEditCategorySheet.Save -> TODO()
        }
    }

}
