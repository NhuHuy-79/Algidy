package com.nhuhuy.algidy.feature.settings.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.rememberNavBackStack
import com.nhuhuy.algidy.core.presentation.navigation.Destination

@Composable
fun SettingGraph() {

    rememberNavBackStack(Destination.Setting.Main)

}