package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryUiState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InventoryVerticalToolbar(
    state: InventoryUiState,
    onExpandChange: () -> Unit,
    onBarcodeScanClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAddManuallyClick: () -> Unit,
    onAction: (InventoryAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }
    VerticalFloatingToolbar(
        modifier = modifier,
        expanded = true,
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
            toolbarContainerColor = scheme.secondaryContainer,
            toolbarContentColor = scheme.onSecondaryContainer
        ),
        leadingContent = {
            FilledIconButton(
                onClick = onExpandChange,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (!state.expanded) scheme.primary else scheme.secondary,
                    contentColor = if (!state.expanded) scheme.onPrimary else scheme.onSecondary
                )
            ) {
                AppIcon(
                    iconProvider = if (state.expanded) AlgidyIcons.Close else AlgidyIcons.Inventory.AddFood
                )
            }
        }
    ) {
        AnimatedVisibility(
            visible = state.expanded
        ) {
            IconButton(
                onClick = onBarcodeScanClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = scheme.primary,
                    contentColor = scheme.onPrimary
                ),
                shape = RoundedCornerShape(
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp,
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
            ) {
                AppIcon(iconProvider = AlgidyIcons.Inventory.ScanFood)
            }
        }

        AnimatedVisibility(
            visible = state.expanded
        ) {
            IconButton(
                onClick = onAddManuallyClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = scheme.primary,
                    contentColor = scheme.onPrimary
                ),
                shape = RoundedCornerShape(
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp,
                    topStart = 8.dp,
                    topEnd = 8.dp
                )
            ) {
                AppIcon(iconProvider = AlgidyIcons.Inventory.EditFood)
            }
        }

        AnimatedVisibility(
            visible = expanded
        ) {
            Column {
                IconButton(onClick = onSearchClick) {
                    AppIcon(iconProvider = AlgidyIcons.Inventory.SearchFood)
                }

                FilterSortMenu(
                    isExpiredOnlyActive = state.showExpiredOnly,
                    currentSortMode = state.sortMode,
                    onAction = onAction
                )

                CategoryActionMenu(
                    isCategoryEnabled = state.showCategoryEdit,
                    onAction = onAction
                )
            }
        }

        IconButton(onClick = { expanded = !expanded }) {
            AppIcon(
                iconProvider = if (expanded) AlgidyIcons.Inventory.CloseToolbar else AlgidyIcons.Inventory.ExpandToolbar
            )
        }
    }
}