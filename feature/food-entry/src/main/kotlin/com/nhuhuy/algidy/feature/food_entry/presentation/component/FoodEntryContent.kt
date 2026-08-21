package com.nhuhuy.algidy.feature.food_entry.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.food_entry.presentation.model.EntryUiModel
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryUiState
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FoodEntryContent(
    modifier: Modifier = Modifier,
    state: FoodEntryUiState,
    onAction: (FoodEntryAction) -> Unit,
) {
    val entry: EntryUiModel = state.entry
    val localSpacing = LocalAlgidySpacing.current

    Column(
        modifier = modifier,
    ) {
        ImageAndNameField(
            modifier = Modifier.fillMaxWidth(),
            imageUri = entry.imageUri,
            name = entry.name,
            onImageUriChange = { uri -> onAction(FoodEntryAction.OnImagePick(uri)) },
            onEditClick = { onAction(FoodEntryAction.OnEditNameClick) }
        )

        Spacer(modifier = Modifier.height(localSpacing.large))

        //Expiry Date

        ExpiryDateField(
            itemPosition = ItemPosition.TOP,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            expiryDate = entry.expiryDate,
            onEditClick = { }
        )

        Spacer(modifier = Modifier.height(localSpacing.extraSmall))

        //Category field
        CategoryField(
            itemPosition = ItemPosition.MIDDLE,
            currentCategory = entry.categoryUiModel,
            categories = state.categories.toImmutableList(),
            onCategorySelect = { category ->
                onAction(FoodEntryAction.OnCategorySelect(category))
            }
        )

        Spacer(modifier = Modifier.height(localSpacing.extraSmall))

        StorageLocationField(
            itemPosition = ItemPosition.BOTTOM,
            currentLocation = entry.location,
            onLocationSelect = { location ->
                onAction(FoodEntryAction.OnStorageLocationChange(location))
            }
        )

        Spacer(modifier = Modifier.height(localSpacing.extraExtraLarge))

        AddFoodButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAction(FoodEntryAction.OnSaveClick)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AddFoodButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val localShapes = LocalAlgidyShapes.current
    Button(
        modifier = modifier,
        onClick = onClick,
        shapes = ButtonDefaults.shapes(
            shape = localShapes.extraExtraLarge,
            pressedShape = localShapes.extraLarge
        )
    ) {
        Text(
            text = stringResource(R.string.action_add),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}
