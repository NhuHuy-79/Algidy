package com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiEvent

sealed interface FoodEntryEvent : UiEvent {
    data object AskNotificationPermission : FoodEntryEvent
}