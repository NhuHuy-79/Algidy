package com.nhuhuy.algidy.feature.food_entry.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nhuhuy.algidy.core.designsystem.component.AppFilterButton
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.animatedHorizontalShape
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CategoryField(
    itemPosition: ItemPosition,
    currentCategory: CategoryUiModel,
    categories: ImmutableList<CategoryUiModel.ByCategory>,
    onCategorySelect: (category: CategoryUiModel.ByCategory) -> Unit,
    onNewCategoryAdd: () -> Unit
) {
    val localSpacing = LocalAlgidySpacing.current
    ToggleListItem(
        modifier = Modifier.fillMaxWidth(),
        shape = itemPosition.toVerticalSegmentedShape(),
        title = stringResource(R.string.food_entry_category),
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
                val selected = category == currentCategory
                AppFilterButton(
                    selected = category == currentCategory,
                    label = category.data.name,
                    onClick = { onCategorySelect(category) },
                    shape = itemPosition.animatedHorizontalShape(selected = selected)
                )
            }

            item {
                FilledTonalIconButton(
                    onClick = onNewCategoryAdd
                ) {
                    AppIcon(iconProvider = AlgidyIcons.FoodEntry.AddCategory)
                }
            }
        }
    }
}