package com.nhuhuy.algidy.feature.inventory.presentation.shared

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.MarkFoodAsConsumedUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.MarkFoodAsWastedUseCase
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.shared.viewmodel.DetailAction
import com.nhuhuy.algidy.feature.inventory.presentation.shared.viewmodel.DetailEvent
import com.nhuhuy.algidy.feature.inventory.presentation.shared.viewmodel.DetailOverlay
import com.nhuhuy.algidy.feature.inventory.presentation.shared.viewmodel.DetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailBottomSheetViewModel(
    private val foodItem: FoodUiModel,
    private val markFoodAsConsumedUseCase: MarkFoodAsConsumedUseCase,
    private val markFoodAsWastedUseCase: MarkFoodAsWastedUseCase
) : BaseViewModel<DetailUiState, DetailEvent, DetailAction>() {
    private val _uiState = MutableStateFlow(
        DetailUiState(
            currentFoodItem = foodItem
        )
    )
    override val uiState: StateFlow<DetailUiState>
        get() = _uiState.asStateFlow()

    override fun onAction(action: DetailAction) {
        when (action) {
            DetailAction.OnConsumeClick -> {
                _uiState.product { copy(overlay = DetailOverlay.ConsumeConfirm) }
                emitEvent(DetailEvent.OnDismiss)
            }

            DetailAction.OnConsumeConfirm -> {
                viewModelScope.launch {
                    markFoodAsConsumedUseCase(foodItem.id)
                    _uiState.product { copy(overlay = DetailOverlay.None) }
                }
            }

            DetailAction.OnDismiss -> {
                _uiState.product { copy(overlay = DetailOverlay.None) }
            }

            DetailAction.OnEditClick -> {
                _uiState.product { copy(overlay = DetailOverlay.None) }
                emitEvent(DetailEvent.NavigateToEdit)
            }

            DetailAction.OnWasteClick -> {
                _uiState.product { copy(overlay = DetailOverlay.WasteConfirm) }
                emitEvent(DetailEvent.OnDismiss)
            }

            DetailAction.OnWasteConfirm -> viewModelScope.launch {
                markFoodAsWastedUseCase(foodItem.id)
                _uiState.product { copy(overlay = DetailOverlay.None) }
            }
        }
    }
}
