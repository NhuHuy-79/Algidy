package com.nhuhuy.algidy.feature.inventory.presentation.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InventoryFabGroup(
    modifier: Modifier = Modifier,
    onCameraClick: () -> Unit,
    onAddClick: () -> Unit,
    visible: Boolean,
) {
    val localSpacing = LocalAlgidySpacing.current
    val algidyIcon = AlgidyIcons.Inventory
    val scheme = MaterialTheme.colorScheme
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(localSpacing.medium)
    ) {
        FloatingActionButton(
            modifier = Modifier.animateFloatingActionButton(
                visible = visible,
                alignment = Alignment.BottomEnd
            ),
            onClick = onCameraClick,
            containerColor = scheme.tertiaryContainer,
            contentColor = scheme.onTertiaryContainer
        ) {
            AppIcon(iconProvider = algidyIcon.ScanFood)
        }

        MediumFloatingActionButton(
            modifier = Modifier.animateFloatingActionButton(
                visible = visible,
                alignment = Alignment.BottomEnd
            ),
            containerColor = scheme.primaryContainer,
            contentColor = scheme.onPrimaryContainer,
            onClick = onAddClick
        ) {
            AppIcon(
                modifier = Modifier.size(32.dp),
                iconProvider = algidyIcon.AddFood
            )
        }
    }

}