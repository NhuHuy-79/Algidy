package com.nhuhuy.algidy.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow


interface UiEvent

interface UiAction

interface UiState

abstract class BaseViewModel<S : UiState, E : UiEvent, A : UiAction> : ViewModel() {
    abstract val uiState: StateFlow<S>
    protected val currentState: S get() = uiState.value

    @OptIn(InternalCoroutinesApi::class)
    private val _uiEvent = Channel<E>(onBufferOverflow = BufferOverflow.SUSPEND)
    val uiEvent = _uiEvent.receiveAsFlow()

    abstract fun onAction(action: A)
}