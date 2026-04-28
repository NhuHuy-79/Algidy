package com.nhuhuy.algidy.feature.detail.presentation.detail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.AppTextField
import com.nhuhuy.algidy.core.model.ItemUnit
import com.nhuhuy.algidy.core.model.StorageLocation
import com.nhuhuy.algidy.core.presentation.component.AppDatePickerDialog
import com.nhuhuy.algidy.core.presentation.component.asString
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailAction
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailAction.EditEntryAction
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.EditEntryError
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.EditEntryUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class ActiveDatePicker { NONE, PURCHASE, EXPIRY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFoodBottomSheet(
    editEntry: EditEntryUiState,
    errorState: EditEntryError,
    onAction: (DetailAction) -> Unit,
) {
    var activeDatePicker by remember { mutableStateOf(ActiveDatePicker.NONE) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var menuExpanded by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = { onAction(DetailAction.OnDismiss) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Edit Information",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            AppTextField(
                value = editEntry.name,
                onValueChange = { onAction(EditEntryAction.OnNameChange(it)) },
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
                    value = if (editEntry.quantity == 0.0) "" else "${editEntry.quantity}",
                    onValueChange = { text ->
                        val qty = text.toDoubleOrNull() ?: 0.0
                        onAction(EditEntryAction.OnQuantityChange(qty))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = "Quantity",
                    isError = errorState.isQuantityError,
                    errorMessage = errorState.quantityValidation.asString()
                )

                Box(modifier = Modifier.weight(0.4f)) {
                    AppTextField(
                        value = editEntry.itemUnit.name.capitalize(),
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
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.3f)
                    ) {
                        ItemUnit.entries.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit.name.capitalize()) },
                                onClick = {
                                    onAction(EditEntryAction.OnItemUnitChange(unit))
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
                        value = dateFormatter.format(Date(editEntry.purchaseDate)),
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
                        value = if (editEntry.expiryDate == -1L) "Select Date" else dateFormatter.format(
                            Date(editEntry.expiryDate)
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
                value = editEntry.notes,
                onValueChange = { onAction(EditEntryAction.OnNoteChange(it)) },
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
                            selected = editEntry.location == loc,
                            onClick = { onAction(EditEntryAction.OnStorageLocationChange(loc)) },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = StorageLocation.entries.size
                            ),
                            label = { Text(loc.name.capitalize()) }
                        )
                    }
                }
            }

            Button(
                onClick = { onAction(EditEntryAction.OnSave) },
                enabled = errorState.valid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Save Changes", fontWeight = FontWeight.Bold)
            }
        }
    }

    when (activeDatePicker) {
        ActiveDatePicker.PURCHASE -> {
            AppDatePickerDialog(
                initialDateMillis = editEntry.purchaseDate,
                title = "Select Purchase Date",
                onDateSelected = { onAction(EditEntryAction.OnPurchaseDateChange(it)) },
                onDismiss = { activeDatePicker = ActiveDatePicker.NONE }
            )
        }

        ActiveDatePicker.EXPIRY -> {
            AppDatePickerDialog(
                initialDateMillis = if (editEntry.expiryDate == -1L) null else editEntry.expiryDate,
                title = "Select Expiry Date",
                onDateSelected = { onAction(EditEntryAction.OnExpiryDateChange(it)) },
                onDismiss = { activeDatePicker = ActiveDatePicker.NONE }
            )
        }

        ActiveDatePicker.NONE -> {}
    }
}
