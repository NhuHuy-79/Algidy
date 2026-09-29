package com.nhuhuy.algidy.deeplink

internal object FoodDetailMatcher : DeepLinkMatcher {
    private val foodDetailRegex = Regex("^$escapedDomain/food/([^/]+)/?$")

    override fun match(uri: String?): DeepLinkResult {
        if (uri.isNullOrBlank()) return DeepLinkResult.OpenHome

        return foodDetailRegex.find(uri)?.let { result ->
            val foodId = result.groupValues[1]
            DeepLinkResult.OpenFood(foodId = foodId)
        } ?: DeepLinkResult.OpenHome
    }
}