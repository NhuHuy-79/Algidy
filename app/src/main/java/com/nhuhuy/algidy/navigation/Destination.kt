package com.nhuhuy.algidy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.Inventory
import androidx.compose.material.icons.rounded.Reviews
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination : NavKey {
    @Serializable
    data object Inventory : Destination

    @Serializable
    data class Detail(val foodItemId: String) : Destination

    @Serializable
    data object Analytics : Destination

    @Serializable
    data object Review : Destination

    @Serializable
    data object Scanner : Destination
}

val destinations = listOf(
    Destination.Inventory,
    Destination.Analytics,
    Destination.Review,
)

fun Destination.toBottomBarIcon(): ImageVector {
    return when (this) {
        Destination.Analytics -> Icons.Rounded.Analytics
        is Destination.Detail -> Icons.Rounded.AcUnit
        Destination.Inventory -> Icons.Rounded.Inventory
        Destination.Review -> Icons.Rounded.Reviews
        Destination.Scanner -> Icons.Rounded.Camera
    }
}