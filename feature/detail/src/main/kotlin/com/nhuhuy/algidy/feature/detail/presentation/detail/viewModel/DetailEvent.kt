package com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiEvent

sealed interface DetailEvent : UiEvent {
    data object OnImageChangeFailed : DetailEvent
}
