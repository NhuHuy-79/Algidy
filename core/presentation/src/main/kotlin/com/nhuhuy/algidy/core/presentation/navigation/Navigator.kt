package com.nhuhuy.algidy.core.presentation.navigation

import androidx.compose.runtime.Stable
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface Navigator {
    val event: Flow<NavigateEvent>
    fun navigateBack()
    fun navigateTo(destination: Destination)
}

class NavigatorImpl : Navigator {
    private val _event =
        Channel<NavigateEvent>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val event = _event.receiveAsFlow()

    override fun navigateBack() {
        emit(NavigateEvent.NavigateBack)
    }

    override fun navigateTo(destination: Destination) {
        emit(NavigateEvent.NavigateTo(destination))
    }

    private fun emit(event: NavigateEvent) {
        _event.trySend(event)
    }
}

@Stable
sealed interface NavigateEvent {
    data object NavigateBack : NavigateEvent
    data class NavigateTo(val destination: Destination) : NavigateEvent
}