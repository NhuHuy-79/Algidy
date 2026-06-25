package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySelectAction

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InventorySelectBar(
    selectedCount: Int,
    modifier: Modifier = Modifier,
    onAction: (InventoryAction) -> Unit
) {
    MediumFlexibleTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "$selectedCount",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black
                )
            )
        },
        subtitle = {
            Text(
                text = pluralStringResource(R.plurals.inventory_selected_title, selectedCount)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onAction(InventorySelectAction.ClearSelection)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onAction(InventorySelectAction.SelectAll)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.SelectAll,
                    contentDescription = null
                )
            }

            IconButton(
                onClick = {
                    onAction(InventorySelectAction.ConsumeAll)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Restaurant,
                    contentDescription = null
                )
            }

            IconButton(
                onClick = {
                    onAction(InventorySelectAction.WasteAll)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null
                )
            }
        }
    )
}