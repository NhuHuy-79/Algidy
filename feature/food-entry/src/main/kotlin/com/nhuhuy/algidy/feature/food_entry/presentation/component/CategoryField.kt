package com.nhuhuy.algidy.feature.food_entry.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nhuhuy.algidy.core.designsystem.component.AppFilterButton
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toHorizontalSegmentedShape
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CategoryField(
    itemPosition: ItemPosition,
    currentCategory: CategoryUiModel,
    categories: ImmutableList<CategoryUiModel.ByCategory>,
    onCategorySelect: (category: CategoryUiModel.ByCategory) -> Unit
) {
    val localSpacing = LocalAlgidySpacing.current
    ToggleListItem(
        modifier = Modifier.fillMaxWidth(),
        shape = itemPosition.toVerticalSegmentedShape(),
        title = "Category",
        icon = AlgidyIcons.Inventory.Category.toImageVector(),
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(localSpacing.extraSmall),
        ) {
            itemsIndexed(
                items = categories,
                key = { _, category: CategoryUiModel.ByCategory -> category.data.id }
            ) { index: Int, category: CategoryUiModel.ByCategory ->
                val itemPosition = index.toItemPosition(categories.size)
                AppFilterButton(
                    selected = category == currentCategory,
                    label = category.data.name,
                    onClick = { onCategorySelect(category) },
                    shape = itemPosition.toHorizontalSegmentedShape()
                )
            }
        }
    }
}