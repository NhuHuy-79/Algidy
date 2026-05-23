package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.ModeEdit
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppFilterButton
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.model.CategoryUiModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun InventoryCategoryFilter(
    modifier: Modifier = Modifier,
    selectedCategory: CategoryUiModel,
    categories: ImmutableList<CategoryUiModel>,
    onCategoryClick: (CategoryUiModel) -> Unit,
    onCategoryEditClick: () -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = categories,
        ) { category ->
            AppFilterButton(
                selected = selectedCategory == category,
                label = category.toUiText(),
                onClick = {
                    onCategoryClick(category)
                }
            )
        }

        item {
            FilledTonalIconButton(
                onClick = onCategoryEditClick,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun CategoryUiModel.toUiText(): String {
    return when (this) {
        CategoryUiModel.All -> stringResource(R.string.category_all)
        is CategoryUiModel.ByCategory -> this.data.name
    }
}