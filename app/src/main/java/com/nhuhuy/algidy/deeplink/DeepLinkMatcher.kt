package com.nhuhuy.algidy.deeplink

import androidx.compose.runtime.Stable

const val DOMAIN_URI = "https://algidy.app"
val escapedDomain = Regex.escape(DOMAIN_URI)

interface DeepLinkMatcher {
    fun match(uri: String?): DeepLinkResult
}

@Stable
sealed interface DeepLinkResult {
    data class OpenFood(val foodId: String?) : DeepLinkResult
    data object OpenHome : DeepLinkResult
}