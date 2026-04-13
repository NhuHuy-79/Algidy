package com.nhuhuy.algidy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Reviews
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object InventoryRoute : Route

    @Serializable
    data object DetailRoute : Route

    @Serializable
    data object AnalyticsRoute : Route

    @Serializable
    data object ReviewRoute : Route

    @Serializable
    data object ScannerRoute : Route
}

val routes = listOf(
    Route.InventoryRoute,
    Route.AnalyticsRoute,
    Route.ReviewRoute,
)

fun Route.toBottomBarIcon(): ImageVector {
    return when (this) {
        Route.AnalyticsRoute -> Icons.Rounded.Analytics
        Route.DetailRoute -> Icons.Rounded.AcUnit
        Route.InventoryRoute -> Icons.Rounded.Home
        Route.ReviewRoute -> Icons.Rounded.Reviews
        Route.ScannerRoute -> Icons.Rounded.Camera
    }
}