@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.ModeEdit
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.horizontalRoundedCornerShape
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySortMode

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InventoryTopBar(
    isExpiredOnlyActive: Boolean,
    categoryEnabled: Boolean,
    showCategoryEditMode: Boolean,
    currentSortMode: InventorySortMode,
    onAction: (InventoryAction) -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    MediumFlexibleTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(text = stringResource(R.string.inventory_title))
        },
        subtitle = {
            Text(text = stringResource(R.string.inventory_subtitle))
        },
        actions = {
            FilledIconButton(
                onClick = { onAction(InventoryAction.OnSearchClick) },
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null
                )
            }

            if (categoryEnabled && showCategoryEditMode) {
                CategoryActionMenu(onAction = onAction)
            }

            FilterSortMenu(
                isExpiredOnlyActive = isExpiredOnlyActive,
                currentSortMode = currentSortMode,
                onAction = onAction
            )
        }
    )
}

@Composable
private fun CategoryActionMenu(
    onAction: (InventoryAction) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        FilledTonalIconButton(
            shape = horizontalRoundedCornerShape(
                end = 8.dp,
                start = 24.dp
            ),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Rounded.ModeEdit,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.inventory_category_edit)) },
                onClick = {
                    onAction(InventoryAction.OnEditCategorySheet.Open)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.inventory_category_delete)) },
                onClick = {
                    onAction(InventoryAction.OnDeleteCategory)
                    expanded = false
                }
            )
        }
    }
}

@Composable
private fun FilterSortMenu(
    isExpiredOnlyActive: Boolean,
    currentSortMode: InventorySortMode,
    onAction: (InventoryAction) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        FilledTonalIconButton(
            shape = horizontalRoundedCornerShape(
                start = 8.dp,
                end = 24.dp
            ),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Rounded.FilterList,
                contentDescription = stringResource(R.string.inventory_menu_filter),
                tint = if (isExpiredOnlyActive || currentSortMode != InventorySortMode.NONE) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            // Sort by Expiry
            DropdownMenuItem(
                text = { Text(stringResource(R.string.inventory_menu_sort_expiry)) },
                leadingIcon = { Icon(Icons.Rounded.Event, null, Modifier.size(18.dp)) },
                trailingIcon = {
                    if (currentSortMode == InventorySortMode.BY_EXPIRY) {
                        Icon(Icons.Rounded.Check, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                onClick = {
                    onAction(InventoryAction.OnSortByExpiry)
                    expanded = false
                }
            )
            
            // Sort by Name
            DropdownMenuItem(
                text = { Text(stringResource(R.string.inventory_menu_sort_name)) },
                leadingIcon = { Icon(Icons.Rounded.SortByAlpha, null, Modifier.size(18.dp)) },
                trailingIcon = {
                    if (currentSortMode == InventorySortMode.BY_NAME) {
                        Icon(Icons.Rounded.Check, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                onClick = {
                    onAction(InventoryAction.OnSortByName)
                    expanded = false
                }
            )

            // Show Expired Only
            DropdownMenuItem(
                text = { Text(stringResource(R.string.inventory_menu_expired_only)) },
                leadingIcon = { Icon(Icons.Rounded.WarningAmber, null, Modifier.size(18.dp)) },
                trailingIcon = {
                    if (isExpiredOnlyActive) {
                        Icon(Icons.Rounded.Check, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                onClick = {
                    onAction(InventoryAction.OnShowExpiredOnly)
                    expanded = false
                }
            )

            HorizontalDivider()

            // Reset
            DropdownMenuItem(
                text = { Text(stringResource(R.string.inventory_menu_reset)) },
                leadingIcon = { Icon(Icons.Rounded.RestartAlt, null) },
                onClick = {
                    onAction(InventoryAction.OnResetFilters)
                    expanded = false
                }
            )
        }
    }
}
