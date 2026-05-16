package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.delegate.FoodEntryDelegate
import com.nhuhuy.algidy.core.presentation.delegate.FoodEntryDelegateImpl
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.inventory.domain.usecase.CreateFoodItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val repository: FoodRepository,
    private val foodEntryDelegateImpl: FoodEntryDelegateImpl,
    private val createFoodItemUseCase: CreateFoodItemUseCase,
) : BaseViewModel<InventoryUiState, InventoryEvent, InventoryAction>(),
    FoodEntryDelegate by foodEntryDelegateImpl {
    private val _uiState = MutableStateFlow(InventoryUiState())
    override val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    val resultState: StateFlow<InventoryResultState> = repository.observeFoodItems()
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
                    repository.removeFoodItem(action.id)
                }
            }

            InventoryAction.OnAddFabClick -> _uiState.product {
                copy(overlay = InventoryOverlay.FOOD_SHEET)
            }

            InventoryAction.OnDismiss -> _uiState.product { copy(overlay = InventoryOverlay.NONE) }
            InventoryAction.OnManuallyClick -> viewModelScope.launch {
                val foodEntry: FoodItem = getResultFoodItem()
                _uiState.product { copy(overlay = InventoryOverlay.NONE) }
                createFoodItemUseCase(foodItem = foodEntry)
            }
        }
    }

}
