@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhuhuy.algidy.feature.inventory.presentation.inventory

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.CategoryFilterGroup
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventorySelectBar
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryTopBar
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list.InventoryCategoryList
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list.InventoryPager
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list.InventoryTabRow
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryCombineState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryFabAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryResultState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySelectAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryUiState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.getDataOrEmpty
import com.nhuhuy.algidy.feature.inventory.utils.GridCategory
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun InventoryScreen(
    uiState: InventoryUiState,
    combineState: InventoryCombineState,
    inventoryResultState: InventoryResultState,
    onAction: (InventoryAction) -> Unit,
) {
    val categories = GridCategory.entries.toImmutableList()
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()
    val localSpacing = LocalAlgidySpacing.current
    val foods = inventoryResultState.getDataOrEmpty()
    val subTitle = if (foods.isEmpty()) {
        stringResource(R.string.inventory_no_foods)
    } else pluralStringResource(R.plurals.inventory_subtitle, foods.size, foods.size)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                AnimatedContent(
                    targetState = uiState.isSelectMode
                ) { selectedMode ->
                    if (!selectedMode) {
                        InventoryTopBar(
                            state = uiState,
                            combineState = combineState,
                            subTitle = subTitle,
                            title = stringResource(R.string.inventory_title),
                            modifier = Modifier.fillMaxWidth(),
                            onAction = onAction
                        )
                    } else {
                        InventorySelectBar(
                            selectedCount = uiState.selectedFoodIds.size,
                            onAction = onAction
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(localSpacing.large))

            if (combineState.categoryEnabled) {
                CategoryFilterGroup(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    selectedCategory = uiState.currentCategory,
                    categories = combineState.categories.toImmutableList(),
                    onCategoryClick = { onAction(InventoryAction.OnCategorySelect(it)) }
                )
            } else {
                InventoryTabRow(
                    categories = categories,
                    selectedTabIndex = pagerState.currentPage,
                    onTabSelected = { index ->
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }

            Spacer(modifier = Modifier.height(localSpacing.small))

            if (combineState.categoryEnabled) {
                InventoryCategoryList(
                    currentCategory = uiState.currentCategory,
                    inventoryResultState = inventoryResultState,
                    sortMode = uiState.sortMode,
                    selectedIds = uiState.selectedFoodIds.toImmutableSet(),
                    showExpiredOnly = uiState.showExpiredOnly,
                    onItemClick = { item ->
                        if (uiState.isSelectMode) {
                            onAction(InventorySelectAction.OnClick(item.id))
                        } else {
                            onAction(InventoryAction.OnItemClick(item))
                        }
                    },
                    onAddManuallyClick = {
                        onAction(InventoryAction.OnEmptyPageClick)
                    },
                    onItemLongClick = { item -> onAction(InventorySelectAction.OnLongClick(id = item.id)) },
                    onFabVisibilityChange = { visibility ->
                        onAction(InventoryFabAction.OnChangeFabVisibility(visibility))
                    }
                )
            } else {
                InventoryPager(
                    pagerState = pagerState,
                    sortMode = uiState.sortMode,
                    selectedIds = uiState.selectedFoodIds.toImmutableSet(),
                    showExpiredOnly = uiState.showExpiredOnly,
                    inventoryResultState = inventoryResultState,
                    onItemClick = { item ->
                        if (uiState.isSelectMode) {
                            onAction(InventorySelectAction.OnClick(item.id))
                        } else {
                            onAction(InventoryAction.OnItemClick(item))
                        }
                    },
                    onAddManuallyClick = { onAction(InventoryAction.OnEmptyPageClick) },
                    onItemLongClick = { item -> onAction(InventorySelectAction.OnLongClick(item.id)) },
                    onFabVisibilityChange = { visibility ->
                        onAction(InventoryFabAction.OnChangeFabVisibility(visibility))
                    }
                )
            }
        }
    }
}

