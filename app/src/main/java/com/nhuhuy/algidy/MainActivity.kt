package com.nhuhuy.algidy

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.navigation.AppGraph
import com.nhuhuy.algidy.utils.BiometricHandler
import com.nhuhuy.algidy.utils.BiometricResult
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
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
            val onAction = viewModel::onAction
            var isUnlocked by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(uiState.isSplashScreen) {
                if (!uiState.isSplashScreen) {
                    if (!uiState.isBiometricLock) {
                        isUnlocked = true
                    } else if (!isUnlocked) {
                        biometricHandler.authenticate().collect { result ->
                            when (result) {
                                BiometricResult.Success -> {
                                    isUnlocked = true
                                }

                                is BiometricResult.Error -> {
                                    isUnlocked = true
                                    //Unsupported Biometric
                                    onAction(AppAction.UpdateBiometricSupported(false))
                                }
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
                    AppGraph(
                        modifier = if (isUnlocked) Modifier else Modifier.blur(
                            radius = 16.dp,
                            edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(8.dp))
                        )
                    )
                }
            }
        }
    }
}

