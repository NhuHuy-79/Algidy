package com.nhuhuy.algidy.core.data.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

fun <T> MutableStateFlow<T>.product(
    block: T.() -> T
) {
    this.update { value ->
        block(value)
    }
}