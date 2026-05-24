package com.nhuhuy.algidy.feature.food_entry.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.CategoryGroup
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import kotlinx.collections.immutable.ImmutableList

/**
 * Section for selecting a category using a horizontal scrollable row.
 * Includes an option to add a new category.
 */
@Composable
fun CategorySection(
    allCategories: ImmutableList<CategoryUiModel>,
    selectedCategory: CategoryUiModel,
    onCategorySelect: (CategoryUiModel.ByCategory) -> Unit,
    onAddNewCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.confirm_label_category),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )

        CategoryGroup(
            selectedCategory = selectedCategory,
            categories = allCategories,
            onCategoryClick = { category ->
                if (category is CategoryUiModel.ByCategory){
                    onCategorySelect(category)
                }
            },
            onCreateCategoryClick = onAddNewCategory
        )
    }
}
