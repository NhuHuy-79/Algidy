package com.nhuhuy.algidy

import android.os.Bundle
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            val isUnlocked by viewModel.isUnlocked.collectAsStateWithLifecycle()
            val biometricTriggerCount by viewModel.biometricTrigger.collectAsStateWithLifecycle()
            LaunchedEffect(uiState.isSplashScreen, biometricTriggerCount) {
                if (!uiState.isSplashScreen) {
                    if (!uiState.isBiometricLock) {
                        onAction(AppAction.UpdateAppUnlock(true))
                    } else if (!isUnlocked) {
                        biometricHandler.authenticate().collect { result ->
                            when (result) {
                                BiometricResult.Success -> {
                                    onAction(AppAction.UpdateAppUnlock(true))
                                }

                                BiometricResult.Failed -> {
                                    Toast.makeText(
                                        this@MainActivity, this@MainActivity.getString(
                                            com.nhuhuy.algidy.core.presentation.R.string.biometric_auth_failed
                                        ), Toast.LENGTH_SHORT
                                    ).show()
                                    onAction(AppAction.UpdateAppUnlock(false))
                                }

                                is BiometricResult.Error -> {
                                    Toast.makeText(
                                        this@MainActivity, this@MainActivity.getString(
                                            com.nhuhuy.algidy.core.presentation.R.string.biometric_auth_failed
                                        ), Toast.LENGTH_SHORT
                                    ).show()
                                    onAction(AppAction.UpdateAppUnlock(false))
                                    when (result) {
                                        BiometricResult.Error.NotSupported -> {
                                            onAction(AppAction.UpdateBiometricSupported(false))
                                        }

                                        else -> {
                                            Toast.makeText(
                                                this@MainActivity, this@MainActivity.getString(
                                                    com.nhuhuy.algidy.core.presentation.R.string.biometric_auth_failed
                                                ), Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }

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
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (isUnlocked) Modifier else Modifier.blur(
                                radius = 16.dp,
                                edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(8.dp))
                            )
                        )
                )
            }
        }
    }

    override fun onResume() {
        viewModel.onAction(AppAction.TriggerBiometric)
        super.onResume()
    }
}

