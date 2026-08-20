package com.nhuhuy.algidy.feature.food_entry.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppButton
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.model.validate.ValidationResult.Companion.isValid
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.asString
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryOverlay
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryUiState
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodEntryForm(
    state: FoodEntryUiState,
    onAction: (FoodEntryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val localSpacing = LocalAlgidySpacing.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(localSpacing.medium)
    ) {
        FoodImageSection(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            imageUri = state.entry.imageUri
        )

        // Section: Basic Name field
        BasicInfoSection(
            name = state.entry.name,
            onNameChange = { onAction(FoodEntryAction.OnNameChange(it)) },
            isNameError = !state.isValid,
            nameErrorMessage = state.nameValidateResult.asString().orEmpty()
        )

        // Section: Storage location segmented buttons
        StorageSection(
            modifier = Modifier.fillMaxWidth(),
            selectedLocation = state.entry.location,
            onLocationChange = { onAction(FoodEntryAction.OnStorageLocationChange(it)) }
        )

        // Section: Category Selection (Horizontal Row)
        CategorySection(
            allCategories = state.categories.toImmutableList(),
            selectedCategory = state.currentCategory,
            onCategorySelect = { category -> onAction(FoodEntryAction.OnCategorySelect(category)) },
            onAddNewCategory = {
                onAction(FoodEntryAction.OnShowOverlay(FoodEntryOverlay.CATEGORY_ADD))
            }
        )

        Spacer(modifier = Modifier.height(localSpacing.large))

        // Section: Purchase and Expiry date pickers
        DateSection(
            expiryDate = state.entry.expiryDate,
            onEditClick = {
                onAction(FoodEntryAction.OnShowOverlay(FoodEntryOverlay.EXPIRY_DATE_PICKER))
            },
            isExpiryError = !state.expiryDateValidateResult.isValid(),
            expiryErrorMessage = state.expiryDateValidateResult.asString().orEmpty()
        )

        Spacer(modifier = Modifier.height(localSpacing.medium))

        AppButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = state.isValid,
            text = stringResource(R.string.action_save),
            icon = Icons.Rounded.CheckCircle,
            onClick = { onAction(FoodEntryAction.OnSaveClick) }
        )
    }
}
