package com.nhuhuy.algidy.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> UiResultContainer(
    modifier: Modifier = Modifier,
    state: UiResult<T>,
    idle: @Composable () -> Unit,
    loading: @Composable () -> Unit,
    success: @Composable (T) -> Unit,
    error: @Composable (UiError) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            UiResult.Idle -> idle()
            UiResult.Loading -> loading()
            is UiResult.Success -> success(state.data)
            is UiResult.Failure -> error(state.error)
        }
    }
}