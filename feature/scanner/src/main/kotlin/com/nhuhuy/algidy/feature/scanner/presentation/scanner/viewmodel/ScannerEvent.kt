package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.UiError

import com.nhuhuy.algidy.core.presentation.viewmodel.UiEvent

sealed interface ScannerEvent : UiEvent {
    data class OnSuccess(val foodItem: FoodItem) : ScannerEvent
    data class OnFailure(val error: UiError) : ScannerEvent
}