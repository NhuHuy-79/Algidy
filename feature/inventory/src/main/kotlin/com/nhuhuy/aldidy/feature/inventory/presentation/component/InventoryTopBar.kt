@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhuhuy.aldidy.feature.inventory.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InventoryTopBar(
    onBackPress: () -> Unit,
    onSortByExpiry: () -> Unit,
    onSortByName: () -> Unit,
    onShowExpiredOnly: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    var expanded by remember { mutableStateOf(false) }

    MediumFlexibleTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(text = "Inventory")
        },
        subtitle = {
            Text(text = "Good morning!")
        },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Rounded.FilterList,
                        contentDescription = "Filter"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    DropdownMenuItem(
                        text = { Text("Sort by Expiry") },
                        leadingIcon = { Icon(Icons.Rounded.Event, null, Modifier.size(18.dp)) },
                        onClick = {
                            onSortByExpiry()
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sort by Name") },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.SortByAlpha,
                                null,
                                Modifier.size(18.dp)
                            )
                        },
                        onClick = {
                            onSortByName()
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Expired Only") },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.WarningAmber,
                                null,
                                Modifier.size(18.dp)
                            )
                        },
                        onClick = {
                            onShowExpiredOnly()
                            expanded = false
                        }
                    )
                }
            }
        }
    )
}
