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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.data.toErrorMessage
import com.nhuhuy.algidy.core.designsystem.component.AppTextField
import com.nhuhuy.algidy.core.model.ItemUnit
import com.nhuhuy.algidy.core.model.StorageLocation
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.EditEntryError
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.EditEntryUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFoodBottomSheet(
    editEntry: EditEntryUiState,
    errorState: EditEntryError,
    onLocationChange: (StorageLocation) -> Unit,
    onNameChange: (String) -> Unit,
    onUnitItemChange: (ItemUnit) -> Unit,
    onQuantityChange: (Double) -> Unit,
    onNoteChange: (String) -> Unit,
    onExpiryDateChange: (Long) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var menuExpanded by remember { mutableStateOf(false) }
    val units = ItemUnit.entries


    ModalBottomSheet(
        onDismissRequest = onDismiss,
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
                onValueChange = { text -> onNameChange(text) },
                label = "Food Name",
                isError = errorState.isNameError,
                errorMessage = errorState.nameValidation.toErrorMessage()
                    ?.let { stringResource(id = it) }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppTextField(
                    modifier = Modifier.weight(0.6f),
                    value = if (editEntry.quantity == 0.0) "" else "${editEntry.quantity}",
                    onValueChange = { text ->
                        if (text.isEmpty()) {
                            onQuantityChange(0.0)
                        } else {
                            text.toDoubleOrNull()?.let { onQuantityChange(it) }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = "Quantity",
                    isError = errorState.isQuantityError,
                    errorMessage = errorState.quantityValidation.toErrorMessage()
                        ?.let { stringResource(id = it) }
                )

                Box(modifier = Modifier.weight(0.4f)) {
                    AppTextField(
                        value = editEntry.itemUnit.name.capitalize(),
                        onValueChange = {},
                        label = "Unit",
                        readOnly = true,
                        trailingIcon = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { menuExpanded = true }
                    )

                    androidx.compose.material3.DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.3f)
                    ) {
                        units.forEach { unit ->
                            androidx.compose.material3.DropdownMenuItem(
                                text = { Text(unit.name.capitalize()) },
                                onClick = {
                                    onUnitItemChange(unit)
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
            }


            Box(modifier = Modifier.fillMaxWidth()) {
                AppTextField(
                    value = if (editEntry.expiryDate == -1L) "Select Date" else dateFormatter.format(
                        Date(editEntry.expiryDate)
                    ),
                    onValueChange = {},
                    label = "Expiry Date",
                    leadingIcon = Icons.Rounded.CalendarToday,
                    readOnly = false,
                    isError = errorState.isExpiryDateError,
                    errorMessage = errorState.expiryDateValidation.toErrorMessage()
                        ?.let { stringResource(id = it) }
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                )
            }

            AppTextField(
                value = editEntry.notes,
                onValueChange = { text -> onNoteChange(text) },
                label = "Notes",
                placeholder = "Add any extra details (e.g. brand, recipes...)",
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
                            onClick = { onLocationChange(loc) },
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
                onClick = {
                    onSave()
                    onDismiss()
                },
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

    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = if (editEntry.expiryDate == -1L) System.currentTimeMillis() else editEntry.expiryDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { time ->
                        onExpiryDateChange(time)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}