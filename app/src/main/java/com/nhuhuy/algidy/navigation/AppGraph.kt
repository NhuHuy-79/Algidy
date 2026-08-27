package com.nhuhuy.algidy.navigation

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
import com.nhuhuy.algidy.feature.analytics.navigation.AnalyticsRoute
import com.nhuhuy.algidy.feature.inventory.navigation.InventoryRoute
import com.nhuhuy.algidy.feature.inventory.navigation.SearchInventoryRoute
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerRoute
import com.nhuhuy.algidy.feature.settings.navigation.SettingRoute
import com.nhuhuy.algidy.transition.AppTransitions
import com.nhuhuy.algidy.transition.toContentTransform
import org.koin.compose.koinInject

@Composable
fun AppGraph(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
) {
    val navigator = koinInject<Navigator>()

    ObserveEffect(navigator.event) { event ->
        when (event) {
            NavigateEvent.NavigateBack -> if (backStack.isNotEmpty()) backStack.removeLastOrNull()
            is NavigateEvent.NavigateTo -> backStack.add(event.destination)
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
            AppTransitions.bottomBarExpressive.toContentTransform()
        },
        popTransitionSpec = {
            AppTransitions.bottomBarExpressive.toContentTransform()
        },
        predictivePopTransitionSpec = {
            AppTransitions.bottomBarExpressive.toContentTransform()
        },
        onBack = { if (backStack.isNotEmpty()) backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Destination.Inventory.Home> {
                InventoryRoute()
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
                    AppTransitions.enterFromBottom.toContentTransform()
                } + NavDisplay.popTransitionSpec {
                    AppTransitions.enterFromTop.toContentTransform()
                } + NavDisplay.predictivePopTransitionSpec {
                    AppTransitions.enterFromTop.toContentTransform()
                }
            ) {
                ScannerRoute(onNavigateBack = { backStack.removeLastOrNull() })
            }

            entry<Destination.Setting>(
                metadata = NavDisplay.transitionSpec {
                    AppTransitions.enterFromRight.toContentTransform()
                } + NavDisplay.popTransitionSpec {
                    AppTransitions.enterFromLeft.toContentTransform()
                } + NavDisplay.predictivePopTransitionSpec {
                    AppTransitions.enterFromLeft.toContentTransform()
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
