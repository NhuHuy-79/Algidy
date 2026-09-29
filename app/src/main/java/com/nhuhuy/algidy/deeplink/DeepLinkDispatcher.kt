package com.nhuhuy.algidy.deeplink

internal class DeepLinkDispatcher(
    private val matchers: List<DeepLinkMatcher> = listOf(FoodDetailMatcher),
) {
    fun dispatch(uri: String?): DeepLinkResult {
        for (matcher in matchers) {
            val result = matcher.match(uri)
            if (result != DeepLinkResult.OpenHome) {
                return result
            }
        }

        return DeepLinkResult.OpenHome
    }
}