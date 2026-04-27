package com.nhuhuy.algidy.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> ObserveEffect(
    flow: Flow<T>,
    block: suspend (T) -> Unit
) {
    val lifecycleObserver = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleObserver) {
        lifecycleObserver.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { data ->
                block(data)
            }
        }
    }
}