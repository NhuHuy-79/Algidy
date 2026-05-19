package com.nhuhuy.algidy.core.presentation.utils

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import java.util.Locale


@SuppressLint("LocalContextConfigurationRead")
@Composable
fun LocalizationWrapper(
    language: AppLanguage,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val locale = Locale.forLanguageTag(language.isoCode)

    val configuration = Configuration(context.resources.configuration).apply {
        setLocale(locale)
    }

    val localizedContext = context.createConfigurationContext(configuration)

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        content = content
    )
}