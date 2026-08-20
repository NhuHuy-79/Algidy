@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Label
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemShapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryCombineState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySortMode
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryUiState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InventoryTopBar(
    modifier: Modifier = Modifier,
    title: String,
    state: InventoryUiState,
    combineState: InventoryCombineState,
    onAction: (InventoryAction) -> Unit,
) {
    val algidyIcon = AlgidyIcons.Inventory

    MediumTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black
                )
            )
        },
        actions = {
            IconButton(
                onClick = { onAction(InventoryAction.OnSearchClick) }
            ) {
                AppIcon(iconProvider = algidyIcon.SearchFood)
            }


            FilterSortMenu(
                isExpiredOnlyActive = state.showExpiredOnly,
                currentSortMode = state.sortMode,
                onAction = onAction
            )

            CategoryActionMenu(
                isCategoryEnabled = combineState.categoryEnabled,
                onAction = onAction
            )
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CategoryActionMenu(
    modifier: Modifier = Modifier,
    isCategoryEnabled: Boolean,
    onAction: (InventoryAction) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val scheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier,
    ) {
        IconButton(
            shape = CircleShape,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (expanded) scheme.primary else scheme.background,
                contentColor = if (expanded) scheme.onPrimary else scheme.onBackground
            ),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Label,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            shape = LocalAlgidyShapes.current.medium,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)

        ) {
            if (isCategoryEnabled) {
                DropdownMenuItem(
                    shape = LocalAlgidyShapes.current.medium,
                    text = { Text(stringResource(R.string.inventory_category_edit)) },
                    onClick = {
                        onAction(InventoryAction.OnEditCategorySheet.Open)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    shape = LocalAlgidyShapes.current.medium,
                    text = { Text(stringResource(R.string.inventory_category_delete)) },
                    onClick = {
                        onAction(InventoryAction.OnDeleteCategory)
                        expanded = false
                    }
                )
            } else {
                DropdownMenuItem(
                    shape = LocalAlgidyShapes.current.medium,
                    text = { Text(stringResource(R.string.inventory_category_add)) },
                    onClick = {
                        onAction(InventoryAction.OnAddCategory.Open)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilterSortMenu(
    modifier: Modifier = Modifier,
    isExpiredOnlyActive: Boolean,
    currentSortMode: InventorySortMode,
    onAction: (InventoryAction) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val scheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
    ) {
        IconButton(
            shape = CircleShape,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (expanded) scheme.primary else scheme.background,
                contentColor = if (expanded) scheme.onPrimary else scheme.onBackground
            ),
            onClick = { expanded = true }
        ) {
            AppIcon(iconProvider = AlgidyIcons.Inventory.FilterFood)
        }

        DropdownMenu(
            expanded = expanded,
            shape = LocalAlgidyShapes.current.medium,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(scheme.surfaceContainer)
        ) {
            // Sort by Expiry
            DropdownMenuItem(
                selected = currentSortMode == InventorySortMode.BY_EXPIRY,
                shapes = MenuItemShapes(
                    shape = LocalAlgidyShapes.current.medium,
                    selectedShape = LocalAlgidyShapes.current.medium
                ),
                colors = MenuDefaults.selectableItemColors(
                    selectedTextColor = scheme.onSecondaryContainer,
                    textColor = scheme.onSurface,
                    containerColor = Color.Transparent,
                    selectedContainerColor = scheme.secondaryContainer,
                    leadingIconColor = scheme.onSurface,
                    selectedLeadingIconColor = scheme.onSecondaryContainer
                ),
                text = { Text(stringResource(R.string.inventory_menu_sort_expiry)) },
                leadingIcon = {
                    AppIcon(iconProvider = AlgidyIcons.Inventory.SortByExpiry)
                },
                onClick = {
                    onAction(InventoryAction.OnSortByExpiry)
                    expanded = false
                }
            )

            // Sort by Name
            DropdownMenuItem(
                selected = currentSortMode == InventorySortMode.BY_NAME,
                shapes = MenuItemShapes(
                    shape = LocalAlgidyShapes.current.medium,
                    selectedShape = LocalAlgidyShapes.current.medium
                ),
                colors = MenuDefaults.selectableItemColors(
                    selectedTextColor = scheme.onSecondaryContainer,
                    textColor = scheme.onSurface,
                    containerColor = Color.Transparent,
                    selectedContainerColor = scheme.secondaryContainer,
                    leadingIconColor = scheme.onSurface,
                    selectedLeadingIconColor = scheme.onSecondaryContainer
                ),
                text = { Text(stringResource(R.string.inventory_menu_sort_name)) },
                leadingIcon = {
                    AppIcon(iconProvider = AlgidyIcons.Inventory.SortByName)
                },
                onClick = {
                    onAction(InventoryAction.OnSortByName)
                    expanded = false
                }
            )

            // Show Expired Only
            DropdownMenuItem(
                selected = isExpiredOnlyActive,
                shapes = MenuItemShapes(
                    shape = LocalAlgidyShapes.current.medium,
                    selectedShape = LocalAlgidyShapes.current.medium
                ),
                colors = MenuDefaults.selectableItemColors(
                    selectedTextColor = scheme.onSecondaryContainer,
                    textColor = scheme.onSurface,
                    containerColor = Color.Transparent,
                    selectedContainerColor = scheme.secondaryContainer,
                    leadingIconColor = scheme.onSurface,
                    selectedLeadingIconColor = scheme.onSecondaryContainer
                ),
                text = { Text(stringResource(R.string.inventory_menu_expired_only)) },
                leadingIcon = {
                    AppIcon(iconProvider = AlgidyIcons.Inventory.ExpiryOnly)
                },
                onClick = {
                    onAction(InventoryAction.OnShowExpiredOnly)
                    expanded = false
                }
            )


            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.padding(horizontal = LocalAlgidySpacing.current.small),
            )

            // Reset
            DropdownMenuItem(
                shape = LocalAlgidyShapes.current.medium,
                text = { Text(stringResource(R.string.inventory_menu_reset)) },
                leadingIcon = {
                    AppIcon(iconProvider = AlgidyIcons.Inventory.ResetFilter)
                },
                onClick = {
                    onAction(InventoryAction.OnResetFilters)
                    expanded = false
                }
            )
        }
    }
}
