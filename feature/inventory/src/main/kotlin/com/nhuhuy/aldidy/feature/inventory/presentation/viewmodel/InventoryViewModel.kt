package com.nhuhuy.aldidy.feature.inventory.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val repository: FoodRepository
) : ViewModel() {
    val uiState: StateFlow<InventoryUiState> = repository.observeFoodItems()
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

    fun removeItem(id: String) {
        viewModelScope.launch {
            repository.removeFoodItem(id)
        }
    }
}
