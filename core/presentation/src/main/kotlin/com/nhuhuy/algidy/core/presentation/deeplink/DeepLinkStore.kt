package com.nhuhuy.algidy.core.presentation.deeplink

class DeepLinkStore {

    private var pendingFoodId: String? = null

    fun submitFood(foodId: String) {
        pendingFoodId = foodId
    }

    fun consumeFood(): String? {
        val result = pendingFoodId
        pendingFoodId = null
        return result
    }
}