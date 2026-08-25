package com.nhuhuy.algidy.feature.inventory.presentation.shared.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction
import com.nhuhuy.algidy.core.presentation.viewmodel.UiEvent
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel

@Immutable
data class DetailUiState(
    val currentFoodItem: FoodUiModel = FoodUiModel(),
    val overlay: DetailOverlay = DetailOverlay.None
) : UiState


sealed interface DetailAction : UiAction {
    data object OnDismiss : DetailAction
    data object OnEditClick : DetailAction
    data object OnConsumeClick : DetailAction
    data object OnWasteClick : DetailAction
    data object OnConsumeConfirm : DetailAction
    data object OnWasteConfirm : DetailAction
}

@Stable
sealed interface DetailEvent : UiEvent {
    data object OnDismiss : DetailEvent
    data class NavigateToEdit(val foodUiModel: FoodUiModel) : DetailEvent
}

@Stable
sealed interface DetailOverlay {
    data object None : DetailOverlay
    data object ConsumeConfirm : DetailOverlay
    data object WasteConfirm : DetailOverlay
}