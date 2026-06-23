package com.nhuhuy.algidy.core.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.nhuhuy.algidy.core.model.food.FoodItem
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination : NavKey {
    @Serializable
    sealed interface Inventory : Destination {
        @Serializable
        data object Home : Inventory

        @Serializable
        data object Search : Inventory
    }

    @Serializable
    data object Analytics : Destination

    @Serializable
    data object Scanner : Destination

    @Serializable
    data class FoodEntry(
        val initialFoodItem: FoodItem? = null
    ) : Destination

    @Serializable
    data object PreSetting : Destination

    @Serializable
    data class Setting(
        val destination: SettingDestination = SettingDestination.Main
    ) : Destination
}


enum class SettingDestination {
    Main,
    Notification,
    Appearance,
    OtherSettings,
    YourData,
    AboutApp
}