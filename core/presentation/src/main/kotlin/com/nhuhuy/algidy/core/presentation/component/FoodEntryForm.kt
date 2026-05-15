package com.nhuhuy.algidy.core.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.AppTextField
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryError
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class ActiveDatePicker { NONE, PURCHASE, EXPIRY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodEntryForm(
    entryState: FoodEntryUiState,
    errorState: FoodEntryError,
    onAction: (FoodEntryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeDatePicker by remember { mutableStateOf(ActiveDatePicker.NONE) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        AppTextField(
            value = entryState.name,
            onValueChange = { onAction(FoodEntryAction.OnNameChange(it)) },
            label = "Food Name",
            isError = errorState.isNameError,
            errorMessage = errorState.nameValidation.asString()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppTextField(
                modifier = Modifier.weight(0.6f),
                value = if (entryState.quantity == 0.0) "" else "${entryState.quantity}",
                onValueChange = { text ->
                    val qty = text.toDoubleOrNull() ?: 0.0
                    onAction(FoodEntryAction.OnQuantityChange(qty))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = "Quantity",
                isError = errorState.isQuantityError,
                errorMessage = errorState.quantityValidation.asString()
            )

            Box(modifier = Modifier.weight(0.4f)) {
                AppTextField(
                    value = entryState.itemUnit.name.capitalize(),
                    onValueChange = {},
                    label = "Unit",
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { menuExpanded = true }
                )

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    ItemUnit.entries.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit.name.capitalize()) },
                            onClick = {
                                onAction(FoodEntryAction.OnItemUnitChange(unit))
                                menuExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(0.5f)) {
                AppTextField(
                    value = dateFormatter.format(Date(entryState.purchaseDate)),
                    onValueChange = {},
                    isError = errorState.isPurchaseDateError,
                    errorMessage = errorState.purchaseDateValidation.asString(),
                    label = "Purchase Date",
                    leadingIcon = Icons.Rounded.CalendarToday,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { activeDatePicker = ActiveDatePicker.PURCHASE }
                )
            }

            Box(modifier = Modifier.weight(0.5f)) {
                AppTextField(
                    value = if (entryState.expiryDate == -1L) "Select Date" else dateFormatter.format(
                        Date(entryState.expiryDate)
                    ),
                    onValueChange = {},
                    label = "Expiry Date",
                    leadingIcon = Icons.Rounded.CalendarToday,
                    readOnly = true,
                    isError = errorState.isExpiryDateError,
                    errorMessage = errorState.expiryDateValidation.asString(),
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { activeDatePicker = ActiveDatePicker.EXPIRY }
                )
            }
        }

        AppTextField(
            value = entryState.notes,
            onValueChange = { onAction(FoodEntryAction.OnNoteChange(it)) },
            label = "Notes",
            placeholder = "Add any extra details...",
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Storage Location",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                StorageLocation.entries.forEachIndexed { index, loc ->
                    SegmentedButton(
                        selected = entryState.location == loc,
                        onClick = { onAction(FoodEntryAction.OnStorageLocationChange(loc)) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = StorageLocation.entries.size
                        ),
                        label = { Text(loc.name.capitalize()) }
                    )
                }
            }
        }
    }

    // Handle Date Pickers
    when (activeDatePicker) {
        ActiveDatePicker.PURCHASE -> {
            AppDatePickerDialog(
                initialDateMillis = entryState.purchaseDate,
                title = "Select Purchase Date",
                onDateSelected = {
                    onAction(FoodEntryAction.OnPurchaseDateChange(it))
                    activeDatePicker = ActiveDatePicker.NONE
                },
                onDismiss = { activeDatePicker = ActiveDatePicker.NONE }
            )
        }

        ActiveDatePicker.EXPIRY -> {
            AppDatePickerDialog(
                initialDateMillis = if (entryState.expiryDate == -1L) null else entryState.expiryDate,
                title = "Select Expiry Date",
                onDateSelected = {
                    onAction(FoodEntryAction.OnExpiryDateChange(it))
                    activeDatePicker = ActiveDatePicker.NONE
                },
                onDismiss = { activeDatePicker = ActiveDatePicker.NONE }
            )
        }

        ActiveDatePicker.NONE -> {}
    }
}
