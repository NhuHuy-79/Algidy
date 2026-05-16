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
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InventoryFabMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onExpandClose: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onManualClick: () -> Unit,
    onSettingClick: () -> Unit,
    onBarcodeScanClick: () -> Unit,
) {
    FloatingActionButtonMenu(
        modifier = modifier,
        expanded = expanded,
        horizontalAlignment = Alignment.End,
        button = {
            MediumFloatingActionButton(
                onClick = onExpandClose,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = if (expanded) Icons.Rounded.Close else Icons.Rounded.Add,
                    contentDescription = "Menu Actions",
                )
            }
        }
    ) {
        FloatingActionButtonMenuItem(
            onClick = onBarcodeScanClick,
            icon = { Icon(Icons.Rounded.Camera, contentDescription = null) },
            text = { Text(text = "Scan Food") },
        )

        FloatingActionButtonMenuItem(
            onClick = onManualClick,
            icon = { Icon(Icons.Rounded.Edit, contentDescription = null) },
            text = { Text(text = "Add Manually") },
        )

        FloatingActionButtonMenuItem(
            onClick = onAnalyticsClick,
            icon = { Icon(Icons.Rounded.Analytics, contentDescription = null) },
            text = { Text(text = "Analytics") },
        )

        FloatingActionButtonMenuItem(
            onClick = onSettingClick,
            icon = { Icon(Icons.Rounded.Settings, contentDescription = null) },
            text = { Text(text = "Setting") },
        )
    }
}