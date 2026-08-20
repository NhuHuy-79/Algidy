package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.R
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryResultState
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
internal fun InventoryGridContent(
    inventoryResultState: InventoryResultState,
    selectedIds: ImmutableSet<String>,
    onItemClick: (FoodUiModel) -> Unit,
    onItemLongClick: (FoodUiModel) -> Unit,
    onAddManuallyClick: () -> Unit,
    onScroll: (Boolean) -> Unit,
    itemProvider: () -> ImmutableList<FoodUiModel>,
) {
    when (inventoryResultState) {
        InventoryResultState.Loading -> LoadingPage(modifier = Modifier.fillMaxSize())

        is InventoryResultState.Empty -> EmptyPage(
            onClick = onAddManuallyClick,
            modifier = Modifier.fillMaxSize()
        )

        is InventoryResultState.Success -> {
            val items = itemProvider()
            if (items.isEmpty()) {
                EmptyPage(
                    onClick = onAddManuallyClick,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                InventoryGridList(
                    items = items,
                    selectedIds = selectedIds,
                    onItemClick = onItemClick,
                    onItemLongClick = onItemLongClick,
                    onScroll = onScroll
                )
            }
        }
    }
}

@Composable
private fun InventoryGridList(
    modifier: Modifier = Modifier,
    items: ImmutableList<FoodUiModel>,
    selectedIds: ImmutableSet<String>,
    onItemClick: (FoodUiModel) -> Unit,
    onItemLongClick: (FoodUiModel) -> Unit = {},
    onScroll: (Boolean) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(
        start = 16.dp,
        top = 16.dp,
        end = 16.dp,
        bottom = 120.dp
    )
) {
    val staggeredGridState = rememberLazyStaggeredGridState()

    LaunchedEffect(staggeredGridState) {
        snapshotFlow { staggeredGridState.firstVisibleItemIndex < 1 }
            .collect { visible -> onScroll(visible) }
    }

    LazyVerticalStaggeredGrid(
        state = staggeredGridState,
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp
    ) {
        items(
            items = items,
            key = { it.id }
        ) { foodItem ->
            val localShape = LocalAlgidyShapes.current
            InventoryFoodGridItem(
                item = foodItem,
                isSelected = foodItem.id in selectedIds,
                modifier = Modifier
                    .animateItem()
                    .clip(localShape.large)
                    .combinedClickable(
                        enabled = true,
                        onClick = { onItemClick(foodItem) },
                        onLongClick = { onItemLongClick(foodItem) }
                    )
            )
        }
    }
}

@Composable
private fun EmptyPage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val spacing = AlgidyTheme.spacing
    val scheme = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .background(color = scheme.background)
            .padding(horizontal = spacing.medium),
        verticalArrangement = Arrangement.spacedBy(
            space = spacing.small,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_empty),
            contentDescription = "empty",
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = stringResource(com.nhuhuy.algidy.core.presentation.R.string.inventory_empty_title),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
            ),
            color = scheme.onBackground,
        )

        Text(
            text = stringResource(com.nhuhuy.algidy.core.presentation.R.string.inventory_empty_content),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = scheme.onBackground
        )

        TextButton(
            onClick = onClick
        ) {
            Text(
                text = stringResource(com.nhuhuy.algidy.core.presentation.R.string.inventory_manually_btn),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LoadingPage(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularWavyProgressIndicator()
    }
}