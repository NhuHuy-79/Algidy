package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiEvent

sealed interface InventoryEvent : UiEvent {
    data object RequestCameraPermission : InventoryEvent
    data object NavigateToScanner : InventoryEvent
}
