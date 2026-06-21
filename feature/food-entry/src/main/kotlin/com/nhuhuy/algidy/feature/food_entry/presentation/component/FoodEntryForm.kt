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
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.asString
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryError
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryOverlay
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryUiState
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodEntryForm(
    entryState: FoodEntryUiState,
    errorState: FoodEntryError,
    onAction: (FoodEntryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section: Image picker and preview
        FoodImageSection(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            imageUri = entryState.imageUri
        )


        // Section: Basic Name field
        BasicInfoSection(
            name = entryState.name,
            onNameChange = { onAction(FoodEntryAction.OnNameChange(it)) },
            isNameError = errorState.isNameError,
            nameErrorMessage = errorState.nameValidation.asString().orEmpty()
        )

        // Section: Storage location segmented buttons
        StorageSection(
            modifier = Modifier.fillMaxWidth(),
            selectedLocation = entryState.location,
            onLocationChange = { onAction(FoodEntryAction.OnStorageLocationChange(it)) }
        )

        // Section: Category Selection (Horizontal Row)
        CategorySection(
            allCategories = entryState.categories.toImmutableList(),
            selectedCategory = entryState.currentCategory,
            onCategorySelect = { category -> onAction(FoodEntryAction.OnCategorySelect(category)) },
            onAddNewCategory = {
                onAction(FoodEntryAction.OnShowOverlay(FoodEntryOverlay.CATEGORY_ADD))
            }
        )

        // Section: Purchase and Expiry date pickers
        DateSection(
            purchaseDate = entryState.purchaseDate,
            expiryDate = entryState.expiryDate,
            onPurchaseClick = {
                onAction(FoodEntryAction.OnShowOverlay(FoodEntryOverlay.PURCHASE_DATE_PICKER))
            },
            onExpiryClick = {
                onAction(FoodEntryAction.OnShowOverlay(FoodEntryOverlay.EXPIRY_DATE_PICKER))
            },
            isPurchaseError = errorState.isPurchaseDateError,
            purchaseErrorMessage = errorState.purchaseDateValidation.asString().orEmpty(),
            isExpiryError = errorState.isExpiryDateError,
            expiryErrorMessage = errorState.expiryDateValidation.asString().orEmpty()
        )


        // Section: Additional notes field
        NotesSection(
            notes = entryState.notes,
            onNoteChange = { onAction(FoodEntryAction.OnNoteChange(it)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            text = stringResource(R.string.action_save),
            icon = Icons.Rounded.CheckCircle,
            onClick = {
                onAction(FoodEntryAction.OnSaveClick)
            }
        )
    }
}
