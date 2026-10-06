package com.nhuhuy.algidy.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nhuhuy.algidy.core.designsystem.component.AppFilterButton
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.segmented_shape.animation.toAnimatedSegmentedShape
import com.nhuhuy.segmented_shape.constant.SegmentedDirection
import com.nhuhuy.segmented_shape.lazy_list.segmentedItems
import kotlinx.collections.immutable.ImmutableList

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
        segmentedItems(
            items = categories,
        ) { category, position ->
            val selected = category == selectedCategory
            AppFilterButton(
                selected = selected,
                label = category.toUiText(),
                onClick = { onCategoryClick(category) },
                modifier = Modifier.animateItem(),
                shape = position.toAnimatedSegmentedShape(
                    direction = SegmentedDirection.HORIZONTAL,
                    animatedToSelected = selected
                )
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