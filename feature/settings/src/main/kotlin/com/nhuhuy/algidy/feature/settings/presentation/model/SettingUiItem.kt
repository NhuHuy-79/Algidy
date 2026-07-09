package com.nhuhuy.algidy.feature.settings.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.model.setting.AppLanguage

sealed interface SettingUiItem

@Immutable
data class SettingClickableItem(
    val type: ClickableType,
) : SettingUiItem

@Stable
sealed interface ClickableType {
    data object DailyReminder : ClickableType
    data object Export : ClickableType
    data object Import : ClickableType
    data class Language(val currentLanguage: AppLanguage) : ClickableType
    data object DeleteAll : ClickableType
    data object AboutApp : ClickableType
    data object NewFeatures : ClickableType
    data object CopyRight : ClickableType
    data object Feedback : ClickableType
    data object PrivacyPolicy : ClickableType
    data object OpenSource : ClickableType
}


@Immutable
data class SettingToggleItem(
    val enable: Boolean = true,
    val type: ToggleType,
    val checked: Boolean = false,
) : SettingUiItem

enum class ToggleType {
    DYNAMIC_COLOR, BIOMETRIC_AUTH, NOTIFICATION, CATEGORY_GROUP, WEEKLY_REPORT
}

