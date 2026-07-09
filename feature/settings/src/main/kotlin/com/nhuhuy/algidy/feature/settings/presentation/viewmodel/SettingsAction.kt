package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import android.net.Uri
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction
import com.nhuhuy.algidy.feature.settings.presentation.model.ClickableType
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingSliderItem
import com.nhuhuy.algidy.feature.settings.presentation.model.ToggleType

sealed interface SettingsAction : UiAction {
    data object OnDismiss : SettingsAction
    sealed interface SetNotifyTime : SettingsAction {
        data object OpenPicker : SetNotifyTime
        data class SetHourAndMinutes(val hour: Int, val minutes: Int) : SetNotifyTime
    }

    data class OnNotificationGranted(val granted: Boolean) : SettingsAction
    data class SetDarkMode(val darkMode: DarkMode) : SettingsAction
    data class ChangeLanguage(val language: AppLanguage) : SettingsAction
    data class ChangeFont(val font: AppFont) : SettingsAction
    data class SetWarningDays(val days: Int) : SettingsAction
    data class SetDeleteThresholdDays(val thresholdDays: Int) : SettingsAction
    data object OnBackClick : SettingsAction
    sealed interface DeleteAlertDialog {
        data object Dismiss : SettingsAction
        data object Confirm : SettingsAction
    }

    data class ImportData(val uri: Uri) : SettingsAction

    data class ClickableAction(
        val type: ClickableType
    ) : SettingsAction


    data class ToggleAction(
        val type: ToggleType,
        val enabled: Boolean
    ) : SettingsAction

    data class SliderAction(
        val value: Int,
        val type: SettingSliderItem
    ) : SettingsAction
}

