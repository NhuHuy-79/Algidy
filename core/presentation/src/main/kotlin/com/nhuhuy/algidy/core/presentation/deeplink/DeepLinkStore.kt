package com.nhuhuy.algidy.core.presentation.deeplink

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class DeepLinkStore {

    private val _foodId = MutableSharedFlow<String>(replay = 1)
    val foodId = _foodId.asSharedFlow()

    fun updateFoodId(foodId: String) {
        _foodId.tryEmit(foodId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun reset() {
        _foodId.resetReplayCache()
    }

}