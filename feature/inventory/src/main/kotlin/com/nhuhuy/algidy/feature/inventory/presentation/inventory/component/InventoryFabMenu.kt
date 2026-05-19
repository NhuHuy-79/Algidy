package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InventoryFabMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onExpandClose: (Boolean) -> Unit,
    onAnalyticsClick: () -> Unit,
    onManualClick: () -> Unit,
    onSettingClick: () -> Unit,
    onBarcodeScanClick: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    FloatingActionButtonMenu(
        modifier = modifier,
        expanded = expanded,
        horizontalAlignment = Alignment.End,
        button = {
            ToggleFloatingActionButton(
                modifier = Modifier,
                containerSize = { value ->
                    lerp(start = 72.dp, stop = 56.dp, fraction = value)
                },
                containerColor = { value ->
                    lerp(
                        start = scheme.primaryContainer,
                        stop = scheme.secondaryContainer,
                        fraction = value
                    )
                },
                checked = expanded,
                onCheckedChange = onExpandClose,
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = if (!expanded) Icons.Rounded.Add else Icons.Rounded.Close,
                    tint = if (expanded) scheme.onSecondaryContainer else scheme.onPrimaryContainer,
                    contentDescription = "Menu Actions",
                )
            }
        }
    ) {
        FloatingActionButtonMenuItem(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = onBarcodeScanClick,
            icon = { Icon(Icons.Rounded.Camera, contentDescription = null) },
            text = { Text(text = "Scan Food") },
        )

        FloatingActionButtonMenuItem(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = onManualClick,
            icon = { Icon(Icons.Rounded.Edit, contentDescription = null) },
            text = { Text(text = "Add Manually") },
        )

        FloatingActionButtonMenuItem(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = onAnalyticsClick,
            icon = { Icon(Icons.Rounded.Analytics, contentDescription = null) },
            text = { Text(text = "Analytics") },
        )

        FloatingActionButtonMenuItem(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = onSettingClick,
            icon = { Icon(Icons.Rounded.Settings, contentDescription = null) },
            text = { Text(text = "Setting") },
        )
    }
}