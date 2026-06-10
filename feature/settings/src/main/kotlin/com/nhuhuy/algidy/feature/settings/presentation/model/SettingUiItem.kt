package com.nhuhuy.algidy.feature.settings.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

sealed interface SettingUiItem

@Immutable
data class SettingClickableItem(
    val type: ClickableType,
) : SettingUiItem

@Stable
sealed interface ClickableType {
    data object Export : ClickableType
    data object Import : ClickableType
    data object DeleteAll : ClickableType
    data object AboutApp : ClickableType
}

@Immutable
data class SettingToggleItem(
    val enable: Boolean = true,
    val type: ToggleType,
    val checked: Boolean = false,
) : SettingUiItem

enum class ToggleType {
    DYNAMIC_COLOR, BIOMETRIC_AUTH, NOTIFICATION, CATEGORY_GROUP
}

