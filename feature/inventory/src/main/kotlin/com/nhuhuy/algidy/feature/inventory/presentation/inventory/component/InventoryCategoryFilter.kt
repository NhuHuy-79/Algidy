package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.model.CategoryUiModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun InventoryCategoryFilter(
    modifier: Modifier = Modifier,
    selectedCategory: CategoryUiModel,
    categories: ImmutableList<CategoryUiModel>,
    onCategoryClick: (CategoryUiModel) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = categories,
        ) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = {
                    onCategoryClick(category)
                },
                label = {
                    Text(
                        text = category.toUiText(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
            )
        }
    }
}

@Composable
fun CategoryUiModel.toUiText(): String {
    return when (this) {
        CategoryUiModel.All -> stringResource(R.string.category_all)
        is CategoryUiModel.ByCategory -> this.data.categoryName
    }
}