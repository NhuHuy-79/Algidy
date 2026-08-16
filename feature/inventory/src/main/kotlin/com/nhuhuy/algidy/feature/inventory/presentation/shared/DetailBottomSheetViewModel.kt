package com.nhuhuy.algidy.feature.inventory.presentation.shared

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.presentation.navigation.Destination
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.MarkFoodAsConsumedUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.MarkFoodAsWastedUseCase
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
data class DetailBottomSheetUiState(
    val foodItem: FoodUiModel,
    val overlay: DetailOverlay = DetailOverlay.None
)

@Stable
sealed interface DetailOverlay {
    data object None : DetailOverlay
    data object ConsumeConfirm : DetailOverlay
    data object WasteConfirm : DetailOverlay
}

class DetailBottomSheetViewModel(
    private val foodItem: FoodUiModel,
    private val markFoodAsConsumedUseCase: MarkFoodAsConsumedUseCase,
    private val markFoodAsWastedUseCase: MarkFoodAsWastedUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailBottomSheetUiState(foodItem = foodItem))
    val uiState: StateFlow<DetailBottomSheetUiState> = _uiState.asStateFlow()

    fun onEditClick(onDismiss: () -> Unit) {
        onDismiss()
        navigator.navigateTo(Destination.FoodEntry(foodId = foodItem.id))
    }

    fun onConsumedClick() {
        _uiState.update { it.copy(overlay = DetailOverlay.ConsumeConfirm) }
    }

    fun onWastedClick() {
        _uiState.update { it.copy(overlay = DetailOverlay.WasteConfirm) }
    }

    fun onDismissOverlay() {
        _uiState.update { it.copy(overlay = DetailOverlay.None) }
    }

    fun onConsumeConfirm(onDismiss: () -> Unit) {
        viewModelScope.launch {
            markFoodAsConsumedUseCase.executeWithList(listOf(foodItem.id))
            onDismiss()
        }
    }

    fun onWasteConfirm(onDismiss: () -> Unit) {
        viewModelScope.launch {
            markFoodAsWastedUseCase.executeWithList(listOf(foodItem.id))
            onDismiss()
        }
    }
}
