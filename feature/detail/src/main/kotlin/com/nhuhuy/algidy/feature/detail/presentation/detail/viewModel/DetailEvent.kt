package com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel

sealed interface DetailEvent {
    data object OnImageChangeFailed : DetailEvent
}
