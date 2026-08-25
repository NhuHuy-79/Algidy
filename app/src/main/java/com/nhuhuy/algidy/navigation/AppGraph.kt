package com.nhuhuy.algidy.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.navigation.Destination
import com.nhuhuy.algidy.core.presentation.navigation.NavigateEvent
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.feature.analytics.presentation.navigation.AnalyticsRoute
import com.nhuhuy.algidy.feature.inventory.navigation.InventoryRoute
import com.nhuhuy.algidy.feature.inventory.navigation.SearchInventoryRoute
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerRoute
import com.nhuhuy.algidy.feature.settings.navigation.SettingRoute
import org.koin.compose.koinInject

@Composable
fun AppGraph(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
) {
    val navigator = koinInject<Navigator>()

    ObserveEffect(navigator.event) { event ->
        when (event) {
            NavigateEvent.NavigateBack -> {
                if (backStack.isNotEmpty()) backStack.removeLastOrNull()
            }

            is NavigateEvent.NavigateTo -> {
                backStack.add(event.destination)
            }
        }
    }
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            (slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(400, easing = EaseOutQuart)
            ) + fadeIn()) togetherWith
                    (slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(400)
                    ) + fadeOut())
        },
        popTransitionSpec = {
            (slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(400)
            ) + fadeIn()) togetherWith
                    (slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(400, easing = EaseOutQuart)
                    ) + fadeOut())
        },
        predictivePopTransitionSpec = {
            (slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn() + scaleIn(initialScale = 0.9f)) togetherWith
                    (slideOutHorizontally(targetOffsetX = { it }) + fadeOut())
        },
        onBack = { if (backStack.isNotEmpty()) backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Destination.Inventory.Home> {
                InventoryRoute(
                )
            }

            entry<Destination.Inventory.Search> {
                SearchInventoryRoute(
                    onNavigateBack = backStack::removeLastOrNull
                )
            }
            entry<Destination.Analytics> {
                AnalyticsRoute()
            }

            entry<Destination.Scanner>(
                metadata = NavDisplay.transitionSpec {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(400, easing = EaseOutQuart)
                    ) + fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))

                } + NavDisplay.popTransitionSpec {
                    EnterTransition.None togetherWith slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(400, easing = EaseInCubic)
                    ) + fadeOut(
                        animationSpec = tween(300)
                    )
                } + NavDisplay.predictivePopTransitionSpec {
                    EnterTransition.None togetherWith slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(400, easing = EaseInCubic)
                    ) + fadeOut(animationSpec = tween(300))
                }
            ) {
                ScannerRoute(
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }

            entry<Destination.Setting>(
                metadata = NavDisplay.transitionSpec {
                    slideInHorizontally(
                        initialOffsetX = { it }, 
                        animationSpec = tween(400, easing = EaseOutQuart)
                    ) + fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))

                } + NavDisplay.popTransitionSpec {
                    EnterTransition.None togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(400, easing = EaseOutQuart)
                            ) + fadeOut(animationSpec = tween(300))

                } + NavDisplay.predictivePopTransitionSpec {
                    EnterTransition.None togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(400, easing = EaseOutQuart)
                            ) + fadeOut(animationSpec = tween(300))
                }
            ) { setting ->
                SettingRoute(
                    destination = setting.destination,
                    onNavigateToSettingRoute = { destination -> backStack.add(destination) },
                    onNavigateBack = backStack::removeLastOrNull,
                )
            }
        }
    )
}
