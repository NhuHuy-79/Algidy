package com.nhuhuy.algidy

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyDynamicTheme
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.navigation.Destination
import com.nhuhuy.algidy.core.presentation.utils.toColor
import com.nhuhuy.algidy.navigation.AppGraph
import com.nhuhuy.algidy.navigation.BottomBarItem
import com.nhuhuy.algidy.navigation.BottomFloatingBar
import com.nhuhuy.algidy.navigation.FloatingBottomBarScaffold
import com.nhuhuy.algidy.navigation.hideBottomBar
import com.nhuhuy.algidy.navigation.toBottomBarItem
import com.nhuhuy.algidy.navigation.toDestination
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
            navigationBarStyle = SystemBarStyle.dark(scrim = Color.TRANSPARENT)
        )
        setContent {
            val backStack = rememberNavBackStack(Destination.Inventory.Home)
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
                                            R.string.biometric_auth_failed
                                        ), Toast.LENGTH_SHORT
                                    ).show()
                                    onAction(AppAction.UpdateAppUnlock(false))
                                }

                                is BiometricResult.Error -> {
                                    Toast.makeText(
                                        this@MainActivity, this@MainActivity.getString(
                                            R.string.biometric_auth_failed
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
                                                    R.string.biometric_auth_failed
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
            AlgidyDynamicTheme(
                seedColor = uiState.seedColor.toColor(),
                dynamicColor = uiState.isDynamicColors,
                darkTheme = when (uiState.darkMode) {
                    DarkMode.DARK -> true
                    DarkMode.LIGHT -> false
                    DarkMode.SYSTEM -> isSystemInDarkTheme()
                }
            ) {
                FloatingBottomBarScaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val currentBottomBarItem = backStack.map { it.toBottomBarItem() }
                            .lastOrNull() ?: BottomBarItem.HOME
                        AnimatedVisibility(
                            visible = !hideBottomBar(currentDestination = backStack.lastOrNull()),
                        ) {
                            BottomFloatingBar(
                                modifier = Modifier,
                                selectedBottomBarItem = currentBottomBarItem,
                                onBottomBarClick = { item ->
                                    val destination = item.toDestination()
                                    backStack.add(destination)
                                }
                            )
                        }
                    },
                ) {
                    AppGraph(
                        modifier = Modifier,
                        backStack = backStack,
                    )
                }
            }
        }
    }

    override fun onResume() {
        viewModel.onAction(AppAction.TriggerBiometric)
        super.onResume()
    }
}

