package com.nhuhuy.algidy

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.navigation.AppGraph
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: AppViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            viewModel.appUiState.value.isSplashScreen
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(
                scrim = android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            val uiState: AppUiState by viewModel.appUiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.language) {
                val currentLocales = AppCompatDelegate.getApplicationLocales()
                if (currentLocales.isEmpty || currentLocales[0]?.language != uiState.language.isoCode) {
                    val appLocale: LocaleListCompat =
                        LocaleListCompat.forLanguageTags(uiState.language.isoCode)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            }
            CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides this) {
                AlgidyTheme(
                    fontName = uiState.font.fontName,
                    dynamicColor = uiState.isDynamicColors,
                    darkTheme = when (uiState.darkMode) {
                        DarkMode.DARK -> true
                        DarkMode.LIGHT -> false
                        DarkMode.SYSTEM -> isSystemInDarkTheme()
                    }
                ) {
                    AppGraph()
                }
            }
        }
    }
}

