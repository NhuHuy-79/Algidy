package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.nhuhuy.algidy.feature.inventory.utils.GridCategory
import com.nhuhuy.algidy.feature.inventory.utils.toStringRes
import kotlinx.collections.immutable.ImmutableList

@Composable
fun InventoryTabRow(
    categories: ImmutableList<GridCategory>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    SecondaryTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = Color.Transparent,
        divider = {},
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = stringResource(category.toStringRes()),
                        style = if (selectedTabIndex == index)
                            MaterialTheme.typography.titleSmall
                        else MaterialTheme.typography.bodyMedium,
                        color = if (selectedTabIndex == index)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }
}
