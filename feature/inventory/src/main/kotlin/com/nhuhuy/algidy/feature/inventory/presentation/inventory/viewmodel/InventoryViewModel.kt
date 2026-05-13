package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val repository: FoodRepository
) : BaseViewModel<InventoryUiState, InventoryEvent, InventoryAction>() {
    override val uiState: StateFlow<InventoryUiState> = repository.observeFoodItems()
        .map { items ->
            if (items.isEmpty()) InventoryUiState.Empty
            else InventoryUiState.Success(items = items)
        }
        .onStart { emit(InventoryUiState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InventoryUiState.Loading
        )

    override fun onAction(action: InventoryAction) {
        when (action) {
            is InventoryAction.RemoveItem -> {
                viewModelScope.launch {
                    repository.removeFoodItem(action.id)
                }
            }
        }
    }
}
