package com.nhuhuy.algidy.feature.settings.presentation.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.designsystem.icon.IconProvider

@Immutable
data class SettingToggleUiModel(
    val type: SettingToggleType,
    @param:StringRes val title: Int,
    @param:StringRes val description: Int,
    val icon: IconProvider,
    val checked: Boolean = false,
    val enabled: Boolean = true
)

enum class SettingToggleType {
    DYNAMIC_COLOR,
    BIOMETRIC_AUTH,
    NOTIFICATION,
    CATEGORY_GROUP,
    WEEKLY_REPORT
}
