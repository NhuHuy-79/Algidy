package com.nhuhuy.algidy.feature.settings.presentation.model

/**
 * Static definitions of setting items grouped by screen.
 */
object SettingItems {

    val MainScreenSection1 = listOf(
        SettingClickableUiModel.APPEARANCE,
        SettingClickableUiModel.NOTIFICATION,
        SettingClickableUiModel.YOUR_DATA,
        SettingClickableUiModel.OTHER_SETTING,
    )

    val MainScreenSection2 = listOf(
        SettingClickableUiModel.NEW_FEATURES,
        SettingClickableUiModel.ABOUT,
        SettingClickableUiModel.DEBUG,
    )

    val DataScreen = listOf(
        SettingClickableUiModel.EXPORT,
        SettingClickableUiModel.IMPORT,
        SettingClickableUiModel.DELETE_ALL
    )

    val AboutScreen = listOf(
        /*  SettingClickableUiModel.NEW_FEATURES,
          SettingClickableUiModel.FEEDBACK,*/
        SettingClickableUiModel.PRIVACY_POLICY,
        SettingClickableUiModel.COPYRIGHT,
        SettingClickableUiModel.CHECK_UPDATE,
        SettingClickableUiModel.OPEN_SOURCE
    )
}
