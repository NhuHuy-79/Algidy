package com.nhuhuy.algidy.core.presentation.preview

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
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FakeBottomFloatingBar(
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
        FakeBottomBarItem(
            icon = AlgidyIcons.BottomBar.UnselectedInventory.toImageVector(),
            label = "Inventory",
            selected = true,
        )

        FakeBottomBarItem(
            icon = AlgidyIcons.BottomBar.SelectedAnalytics.toImageVector(),
            label = "Analytics",
            selected = false,
        )

        FakeBottomBarItem(
            icon = AlgidyIcons.BottomBar.UnselectedSettings.toImageVector(),
            label = "Settings",
            selected = false,
        )
    }
}

@Composable
private fun FakeBottomBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        color = if (selected) {
            scheme.primaryContainer
        } else {
            scheme.primary
        },
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) {
                    scheme.onPrimaryContainer
                } else {
                    scheme.onPrimary
                },
                modifier = Modifier.size(24.dp),
            )

            if (selected) {
                Text(
                    text = label,
                    color = scheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}