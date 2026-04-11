package com.nhuhuy.algidy.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = MintGreenPrimary,
    secondary = MintGreenSecondary,
    tertiary = MintGreenTertiary
)

private val LightColorScheme = lightColorScheme(
    primary = MintGreenPrimary,
    secondary = MintGreenSecondary,
    tertiary = MintGreenTertiary
)

@Composable
fun AlgidyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
