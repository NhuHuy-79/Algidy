package com.nhuhuy.algidy.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutExpo
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.nhuhuy.aldidy.feature.inventory.presentation.InventoryScreen
import com.nhuhuy.algidy.feature.analytics.presentation.component.AnalyticsScreen
import com.nhuhuy.algidy.feature.detail.presentation.component.DetailScreen
import com.nhuhuy.algidy.feature.review.ReviewScreen
import com.nhuhuy.algidy.feature.scanner.ScannerScreen

@Composable
fun AppGraph(
    backStack: SnapshotStateList<Route>,
) {
    NavDisplay(
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
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Route.InventoryRoute> {
                InventoryScreen()
            }

            entry<Route.DetailRoute> {
                DetailScreen()
            }

            entry<Route.AnalyticsRoute> {
                AnalyticsScreen()
            }

            entry<Route.ReviewRoute> {
                ReviewScreen()
            }

            entry<Route.ScannerRoute>(
                metadata = NavDisplay.transitionSpec {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(500, easing = EaseOutExpo)
                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                } + NavDisplay.popTransitionSpec {
                    EnterTransition.None togetherWith slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(450, easing = EaseInOut)
                    )
                } + NavDisplay.predictivePopTransitionSpec {
                    EnterTransition.None togetherWith slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(450, easing = EaseInOut)
                    )
                }
            ) {
                ScannerScreen()
            }
        }
    )
}
