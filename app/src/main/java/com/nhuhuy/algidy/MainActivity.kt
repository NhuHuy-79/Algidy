package com.nhuhuy.algidy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.presentation.utils.LocalizationWrapper
import com.nhuhuy.algidy.navigation.AppGraph
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
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
            LocalizationWrapper(language = uiState.language) {
                AlgidyTheme(
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

