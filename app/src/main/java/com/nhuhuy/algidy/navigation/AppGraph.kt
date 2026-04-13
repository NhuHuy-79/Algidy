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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.nhuhuy.aldidy.feature.inventory.presentation.InventoryRoute
import com.nhuhuy.algidy.feature.analytics.presentation.component.AnalyticsRoute
import com.nhuhuy.algidy.feature.detail.presentation.navigation.DetailRoute
import com.nhuhuy.algidy.feature.review.ReviewRoute
import com.nhuhuy.algidy.feature.scanner.ScannerRoute
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppGraph(
    backStack: SnapshotStateList<Destination>,
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
            entry<Destination.Inventory> {
                InventoryRoute(
                    viewModel = koinViewModel(),
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToDetail = { foodItemId ->
                        backStack.add(Destination.Detail(foodItemId = foodItemId))
                    }
                )
            }

            entry<Destination.Detail> { destinationDetail ->
                DetailRoute(
                    viewModel = koinViewModel(
                        parameters = { parametersOf(destinationDetail.foodItemId) }
                    ),
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }

            entry<Destination.Analytics> {
                AnalyticsRoute()
            }

            entry<Destination.Review> {
                ReviewRoute()
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
                ScannerRoute()
            }
        }
    )
}
