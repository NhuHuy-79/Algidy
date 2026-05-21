package com.nhuhuy.algidy

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import com.nhuhuy.algidy.component.AppRoot
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.utils.BiometricHandler
import com.nhuhuy.algidy.utils.BiometricResult
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : FragmentActivity() {
    private val viewModel: AppViewModel by viewModel()
    private val biometricHandler by lazy { BiometricHandler(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            viewModel.appUiState.value.isSplashScreen
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(scrim = android.graphics.Color.TRANSPARENT)
        )
        setContent {
            val uiState: AppUiState by viewModel.appUiState.collectAsStateWithLifecycle()
            var isUnlocked by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(uiState.isSplashScreen) {
                if (!uiState.isSplashScreen) {
                    if (!uiState.isBiometricLock) {
                        isUnlocked = true
                    } else if (!isUnlocked) {
                        biometricHandler.authenticate().collect { result ->
                            when (result) {
                                BiometricResult.Success -> isUnlocked = true
                                // Nếu thiết bị lỗi/không hỗ trợ, cho vào luôn để tránh kẹt màn hình đen
                                is BiometricResult.Error -> isUnlocked = true
                                BiometricResult.Failed -> isUnlocked = false
                                BiometricResult.Idle -> Unit
                            }
                        }
                    }
                }
            }


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
                    AppRoot(showContent = isUnlocked)
                }
            }
        }
    }
}

