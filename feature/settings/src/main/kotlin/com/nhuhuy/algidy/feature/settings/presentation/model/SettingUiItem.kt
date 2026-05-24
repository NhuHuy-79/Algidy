package com.nhuhuy.algidy.feature.settings.presentation.model

import android.net.Uri
import androidx.compose.runtime.Immutable

sealed interface SettingUiItem

@Immutable
data class SettingClickableItem(
    val type: ClickableType,
) : SettingUiItem


sealed interface ClickableType {
    data object Export : ClickableType
    data class Import(val uri: Uri) : ClickableType
    data object DeleteAll : ClickableType
    data object AboutApp : ClickableType
}

@Immutable
data class SettingToggleItem(
    val visible: Boolean = true,
    val type: ToggleType,
    val enabled: Boolean = false,
) : SettingUiItem

enum class ToggleType {
    DYNAMIC_COLOR, BIOMETRIC_AUTH, NOTIFICATION, CATEGORY_GROUP
}

