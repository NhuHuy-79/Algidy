package com.nhuhuy.algidy.feature.settings.presentation.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.designsystem.icon.IconProvider

@Immutable
data class SettingSliderUiModel(
    val type: SettingSliderType,
    @param:StringRes val title: Int,
    @param:StringRes val description: Int,
    val icon: IconProvider,
    val value: Float,
    val valueRange: ClosedFloatingPointRange<Float>,
    val steps: Int = 0,
    val enabled: Boolean = true,
)

enum class SettingSliderType {
    EXPIRY_WARNING_THRESHOLD,
    EXPIRED_DELETE_THRESHOLD
}
