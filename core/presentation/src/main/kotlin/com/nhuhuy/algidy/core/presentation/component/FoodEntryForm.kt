package com.nhuhuy.algidy.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.component.entry.BasicInfoSection
import com.nhuhuy.algidy.core.presentation.component.entry.CategorySection
import com.nhuhuy.algidy.core.presentation.component.entry.DateSection
import com.nhuhuy.algidy.core.presentation.component.entry.FoodImageSection
import com.nhuhuy.algidy.core.presentation.component.entry.NotesSection
import com.nhuhuy.algidy.core.presentation.component.entry.QuantityUnitSection
import com.nhuhuy.algidy.core.presentation.component.entry.StorageSection
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnImagePick
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnItemUnitChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnNameChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnNoteChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnQuantityChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnStorageLocationChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryError
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryOverlay
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryUiState
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
            imageUri = entryState.imageUri,
            onImagePick = { onAction(OnImagePick(it)) }
        )

        // Section: Basic Name field
        BasicInfoSection(
            name = entryState.name,
            onNameChange = { onAction(OnNameChange(it)) },
            isNameError = errorState.isNameError,
            nameErrorMessage = errorState.nameValidation.asString().orEmpty()
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

        // Section: Quantity input and Unit dropdown
        QuantityUnitSection(
            quantity = entryState.quantity,
            itemUnit = entryState.itemUnit,
            onQuantityChange = { onAction(OnQuantityChange(it)) },
            onUnitChange = { onAction(OnItemUnitChange(it)) },
            isQuantityError = errorState.isQuantityError,
            quantityErrorMessage = errorState.quantityValidation.asString().orEmpty()
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

        // Section: Storage location segmented buttons
        StorageSection(
            selectedLocation = entryState.location,
            onLocationChange = { onAction(OnStorageLocationChange(it)) }
        )

        // Section: Additional notes field
        NotesSection(
            notes = entryState.notes,
            onNoteChange = { onAction(OnNoteChange(it)) }
        )
    }
}
