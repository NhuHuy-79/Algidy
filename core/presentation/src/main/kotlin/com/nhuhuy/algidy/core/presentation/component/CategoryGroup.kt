package com.nhuhuy.algidy.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppFilterButton
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.utils.animatedHorizontalShape
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CategoryGroup(
    modifier: Modifier = Modifier,
    selectedCategory: CategoryUiModel,
    categories: ImmutableList<CategoryUiModel>,
    onCategoryClick: (CategoryUiModel) -> Unit,
    onCreateCategoryClick: () -> Unit = {},
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                onClick = onCreateCategoryClick,
                shape = CircleShape
            ) {
                AppIcon(iconProvider = AlgidyIcons.Inventory.AddCategory)
            }
        }
    }
}

@Composable
fun CategoryFilterGroup(
    modifier: Modifier = Modifier,
    selectedCategory: CategoryUiModel,
    categories: ImmutableList<CategoryUiModel>,
    onCategoryClick: (CategoryUiModel) -> Unit,
) {
    val localSpacing = LocalAlgidySpacing.current
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(localSpacing.extraSmall)
    ) {
        itemsIndexed(
            items = categories,
        ) { index, category ->
            val itemPosition = index.toItemPosition(categories.size)
            val selected = category == selectedCategory
            AppFilterButton(
                selected = selectedCategory == category,
                label = category.toUiText(),
                onClick = { onCategoryClick(category) },
                modifier = Modifier.animateItem(),
                shape = itemPosition.animatedHorizontalShape(selected = selected)
            )
        }
    }
}

@Composable
fun CategoryUiModel.toUiText(): String {
    return when (this) {
        CategoryUiModel.All -> stringResource(R.string.category_all)
        is CategoryUiModel.ByCategory -> this.data.name
        CategoryUiModel.Uncategorized -> stringResource(R.string.category_uncategorized)
    }
}