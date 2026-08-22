package com.nhuhuy.algidy.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.materialkolor.rememberDynamicColorScheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AlgidyDynamicTheme(
    seedColor: Color,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else rememberDynamicColorScheme(
        seedColor = seedColor,
        isDark = darkTheme,
        isAmoled = true
    )

    val cameraColorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val darkScheme = dynamicDarkColorScheme(context)
        val lightScheme = dynamicLightColorScheme(context)
        CameraColorScheme(
            background = darkScheme.surface,
            foreground = darkScheme.onSurface,
            primary = lightScheme.primary,
            onPrimary = lightScheme.onPrimary,
            secondaryContainer = lightScheme.secondary,
            onSecondaryContainer = lightScheme.onSecondary
        )
    } else CameraColorScheme()


    val extendedColors = if (darkTheme) DarkFoodStateColors else LightFoodStateColors
    val dynamicFontFamily = getTypographyForFont()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalFoodStateColors provides extendedColors,
        LocalCameraColorScheme provides cameraColorScheme
    ) {
        MaterialExpressiveTheme(
            typography = dynamicFontFamily,
            colorScheme = colorScheme,
            motionScheme = MotionScheme.expressive(),
            content = content
        )
    }
}