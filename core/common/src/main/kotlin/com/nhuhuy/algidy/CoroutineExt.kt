package com.nhuhuy.algidy

import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

suspend fun runTogetherSuspend(
    vararg blocks: suspend () -> Unit,
) = supervisorScope {
    blocks.forEach { block ->
        launch {
            block()
        }
    }
}