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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.nhuhuy.algidy.feature.analytics.presentation.navigation.AnalyticsRoute
import com.nhuhuy.algidy.feature.detail.presentation.navigation.DetailRoute
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.InventoryRoute
import com.nhuhuy.algidy.feature.inventory.presentation.search.SearchInventoryRoute
import com.nhuhuy.algidy.feature.review.ReviewRoute
import com.nhuhuy.algidy.feature.scanner.presentation.confirm.ConfirmRoute
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerRoute
import com.nhuhuy.algidy.feature.settings.presentation.navigation.SettingRoute

@Composable
fun AppGraph() {
    val backStack = remember { mutableStateListOf<Destination>(Destination.Inventory.Home) }
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
            entry<Destination.Inventory.Home> {
                InventoryRoute(
                    onNavigateToDetail = { foodItemId ->
                        backStack.add(Destination.Detail(foodItemId = foodItemId))
                    },
                    onNavigateToCamera = { backStack.add(Destination.Scanner) },
                    onNavigateToSearch = { backStack.add(Destination.Inventory.Search) },
                    onNavigateToSetting = { backStack.add(Destination.Setting) },
                    onNavigateToAnalytics = {
                        backStack.add(Destination.Analytics)
                    }
                )
            }

            entry<Destination.Inventory.Search> {
                SearchInventoryRoute(
                    onNavigateBack = backStack::removeLastOrNull,
                    onNavigateToDetail = { id ->
                        backStack.add(Destination.Detail(foodItemId = id))
                    }
                )
            }

            entry<Destination.Detail> { destinationDetail ->
                DetailRoute(
                    foodItemId = destinationDetail.foodItemId,
                    onNavigateBack = backStack::removeLastOrNull
                )
            }

            entry<Destination.Analytics> {
                AnalyticsRoute(
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    }
                )
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
                ScannerRoute(
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToConfirm = { id ->
                        backStack.add(Destination.Confirm(foodId = id))
                    }
                )
            }

            entry<Destination.Setting>(
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
                SettingRoute(
                    onNavigateBack = backStack::removeLastOrNull
                )
            }

            entry<Destination.Confirm> { screen ->
                ConfirmRoute(
                    foodItemId = screen.foodId,
                    onNavigateBack = { backStack.removeLastOrNull() },
                )
            }
        }
    )
}
