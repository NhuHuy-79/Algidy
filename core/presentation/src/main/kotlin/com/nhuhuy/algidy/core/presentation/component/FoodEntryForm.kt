package com.nhuhuy.algidy.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.entry.BasicInfoSection
import com.nhuhuy.algidy.core.presentation.component.entry.CategorySection
import com.nhuhuy.algidy.core.presentation.component.entry.DateSection
import com.nhuhuy.algidy.core.presentation.component.entry.FoodImageSection
import com.nhuhuy.algidy.core.presentation.component.entry.NotesSection
import com.nhuhuy.algidy.core.presentation.component.entry.QuantityUnitSection
import com.nhuhuy.algidy.core.presentation.component.entry.StorageSection
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnCategoryConfirm
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnCategoryQueryChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnExpiryDateChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnImagePick
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnItemUnitChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnNameChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnNoteChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnPurchaseDateChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnQuantityChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.OnStorageLocationChange
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryError
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryUiState
import kotlinx.collections.immutable.toImmutableList

private enum class EntryOverlay { NONE, PURCHASE, EXPIRY, ADD_CATEGORY }

/**
 * Orchestrator Composable for the food entry form.
 * Breaks down the form into smaller, manageable sections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodEntryForm(
    entryState: FoodEntryUiState,
    errorState: FoodEntryError,
    onAction: (FoodEntryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var entryOverlay by remember { mutableStateOf(EntryOverlay.NONE) }

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
            onAddNewCategory = { entryOverlay = EntryOverlay.ADD_CATEGORY }
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
            onPurchaseClick = { entryOverlay = EntryOverlay.PURCHASE },
            onExpiryClick = { entryOverlay = EntryOverlay.EXPIRY },
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

    // Overlays Management (Dialogs)
    when (entryOverlay) {
        EntryOverlay.PURCHASE -> {
            AppDatePickerDialog(
                initialDateMillis = entryState.purchaseDate,
                title = stringResource(R.string.confirm_label_purchase_date),
                onDateSelected = {
                    onAction(OnPurchaseDateChange(it))
                    entryOverlay = EntryOverlay.NONE
                },
                onDismiss = { entryOverlay = EntryOverlay.NONE }
            )
        }
        EntryOverlay.EXPIRY -> {
            AppDatePickerDialog(
                initialDateMillis = if (entryState.expiryDate == -1L) null else entryState.expiryDate,
                title = stringResource(R.string.confirm_label_expiry_date),
                onDateSelected = {
                    onAction(OnExpiryDateChange(it))
                    entryOverlay = EntryOverlay.NONE
                },
                onDismiss = { entryOverlay = EntryOverlay.NONE }
            )
        }
        EntryOverlay.ADD_CATEGORY -> {
            TextFieldDialog(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = entryState.categoryQuery,
                title = stringResource(R.string.category_edit_dialog_title),
                label = stringResource(R.string.category_edit_dialog_label),
                confirmText = stringResource(R.string.action_add),
                onValueChange = { onAction(OnCategoryQueryChange(it)) },
                onDismiss = { entryOverlay = EntryOverlay.NONE },
                onConfirm = {
                    onAction(OnCategoryConfirm)
                    entryOverlay = EntryOverlay.NONE
                }
            )
        }
        EntryOverlay.NONE -> {}
    }
}
