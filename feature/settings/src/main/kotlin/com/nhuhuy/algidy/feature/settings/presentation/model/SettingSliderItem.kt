package com.nhuhuy.algidy.feature.settings.presentation.model

import androidx.compose.runtime.Stable

@Stable
sealed interface SettingSliderItem {
    data object ExpiryWarningThreshold : SettingSliderItem
    data object ExpiredDeleteThreshold : SettingSliderItem
}