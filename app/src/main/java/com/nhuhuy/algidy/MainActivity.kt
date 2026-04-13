package com.nhuhuy.algidy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nhuhuy.algidy.component.AppBottomBar
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.navigation.AppGraph
import com.nhuhuy.algidy.navigation.Destination
import com.nhuhuy.algidy.navigation.destinations

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = remember { mutableStateListOf<Destination>(Destination.Inventory) }
            val currentDestination = backStack.lastOrNull() ?: Destination.Inventory

            AlgidyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { paddingValues ->
                    BoxLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    ) {
                        AppGraph(backStack = backStack)

                        AnimatedVisibility(
                            visible = destinations.contains(currentDestination),
                            enter = slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(durationMillis = 400, easing = EaseInOut)
                            ) + fadeIn(),
                            exit = slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(durationMillis = 400, easing = EaseInOut)
                            ) + fadeOut(),
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            AppBottomBar(
                                currentDestination = currentDestination,
                                bottomBarIcons = destinations,
                                onRouteSelected = { route ->
                                    if (currentDestination != route) {
                                        if (backStack.contains(route)) {
                                            backStack.remove(route)
                                        }
                                        backStack.add(route)
                                    }
                                },
                                onScannerPress = { backStack.add(Destination.Scanner) },
                            )
                        }
                    }
                }
            }
        }
    }
}

