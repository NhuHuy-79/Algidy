@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.nhuhuy.algidy.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.IconProvider
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.navigation.Destination
import com.nhuhuy.algidy.core.presentation.navigation.SettingDestination

enum class BottomBarItem(
    @field:StringRes val label: Int,
    val selectedIcon: IconProvider,
    val unselectedIcon: IconProvider
) {
    HOME(
        label = R.string.inventory_title,
        selectedIcon = AlgidyIcons.BottomBar.SelectedInventory,
        unselectedIcon = AlgidyIcons.BottomBar.UnselectedInventory
    ),

    ANALYTICS(
        label = R.string.analytics_title,
        selectedIcon = AlgidyIcons.BottomBar.SelectedAnalytics,
        unselectedIcon = AlgidyIcons.BottomBar.UnselectedAnalytics
    ),

    SETTINGS(
        label = R.string.settings_title,
        selectedIcon = AlgidyIcons.BottomBar.SelectedSettings,
        unselectedIcon = AlgidyIcons.BottomBar.UnselectedSettings
    )
}

fun NavKey.toBottomBarItem(): BottomBarItem? {
    return when (this) {
        Destination.Analytics -> BottomBarItem.ANALYTICS
        is Destination.Inventory, Destination.Scanner -> BottomBarItem.HOME
        is Destination.Setting -> BottomBarItem.SETTINGS
        else -> null
    }
}

fun hideBottomBar(currentDestination: NavKey?): Boolean {
    return when (currentDestination) {
        is Destination.Scanner -> true
        else -> false
    }
}

fun BottomBarItem.toDestination(): Destination {
    return when (this) {
        BottomBarItem.ANALYTICS -> Destination.Analytics
        BottomBarItem.HOME -> Destination.Inventory.Home
        BottomBarItem.SETTINGS -> Destination.Setting(destination = SettingDestination.Main)
    }
}

@Composable
fun BottomFloatingBar(
    selectedBottomBarItem: BottomBarItem,
    onBottomBarClick: (item: BottomBarItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    HorizontalFloatingToolbar(
        modifier = modifier,
        expanded = true,
        colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(
            toolbarContainerColor = scheme.primary,
        ),
    ) {
        BottomBarItem.entries.forEach { bottomBarItem ->
            ToolBarIcon(
                selectedIcon = bottomBarItem.selectedIcon.toImageVector(),
                label = stringResource(bottomBarItem.label),
                unselectedIcon = bottomBarItem.unselectedIcon.toImageVector(),
                selected = bottomBarItem == selectedBottomBarItem,
                onClick = { onBottomBarClick(bottomBarItem) }
            )
        }
    }
}

@Composable
private fun ToolBarIcon(
    modifier: Modifier = Modifier,
    selectedIcon: ImageVector,
    label: String,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedContainerColor: Color = MaterialTheme.colorScheme.primary,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    selectedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    unselectedIcon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.animateContentSize(),
        shape = RoundedCornerShape(32.dp),
        color = if (selected) selectedContainerColor else unselectedContainerColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (selected) selectedIcon else unselectedIcon,
                contentDescription = null,
                tint = if (selected) selectedContentColor else unselectedContentColor,
                modifier = Modifier.size(24.dp)
            )
            AnimatedVisibility(
                visible = selected,
                enter = expandHorizontally(
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                ) + fadeIn(),
                exit = shrinkHorizontally(
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                ) + fadeOut()
            ) {
                Text(
                    text = label,
                    color = selectedContentColor,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
            }

        }
    }
}