package com.nhuhuy.algidy.feature.scanner.presentation.confirm.component

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
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.AppTextField
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.component.AppDatePickerDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class ActiveDatePicker { NONE, PURCHASE, EXPIRY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultBottomSheet(
    foodItem: FoodItem,
    onSave: (FoodItem) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(foodItem.name) }
    var quantity by remember { mutableStateOf(if (foodItem.quantity == 0.0) "" else foodItem.quantity.toString()) }
    var selectedUnit by remember { mutableStateOf(foodItem.itemUnit) }
    var expiryDate by remember { mutableLongStateOf(foodItem.expiryDate) }
    var purchaseDate by remember { mutableLongStateOf(foodItem.purchaseDate) }
    var location by remember { mutableStateOf(foodItem.location) }
    var notes by remember { mutableStateOf(foodItem.notes) }

    var activeDatePicker by remember { mutableStateOf(ActiveDatePicker.NONE) }
    var showUnitMenu by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Confirm Information",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            AppTextField(
                value = name,
                onValueChange = { name = it },
                label = "Food Name",
                placeholder = "Enter food name..."
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppTextField(
                    modifier = Modifier.weight(0.6f),
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = "Quantity",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Box(modifier = Modifier.weight(0.4f)) {
                    AppTextField(
                        value = selectedUnit.name.capitalize(),
                        onValueChange = {},
                        label = "Unit",
                        readOnly = true,
                        trailingIcon = {
                            Icon(Icons.Rounded.ArrowDropDown, contentDescription = null)
                        }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showUnitMenu = true }
                    )
                    DropdownMenu(
                        expanded = showUnitMenu,
                        onDismissRequest = { showUnitMenu = false }
                    ) {
                        ItemUnit.entries.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit.name.lowercase().capitalize()) },
                                onClick = {
                                    selectedUnit = unit
                                    showUnitMenu = false
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
                        value = dateFormatter.format(Date(purchaseDate)),
                        onValueChange = {},
                        label = "Purchase Date",
                        leadingIcon = Icons.Rounded.CalendarToday,
                        readOnly = true
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { activeDatePicker = ActiveDatePicker.PURCHASE }
                    )
                }

                Box(modifier = Modifier.weight(0.5f)) {
                    AppTextField(
                        value = if (expiryDate == -1L) "Select Date" else dateFormatter.format(
                            Date(
                                expiryDate
                            )
                        ),
                        onValueChange = {},
                        label = "Expiry Date",
                        leadingIcon = Icons.Rounded.CalendarToday,
                        readOnly = true
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { activeDatePicker = ActiveDatePicker.EXPIRY }
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Storage Location",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    StorageLocation.entries.forEachIndexed { index, loc ->
                        SegmentedButton(
                            selected = location == loc,
                            onClick = { location = loc },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = StorageLocation.entries.size
                            ),
                            label = { Text(loc.name.lowercase().capitalize()) }
                        )
                    }
                }
            }

            AppTextField(
                value = notes,
                onValueChange = { notes = it },
                label = "Notes",
                placeholder = "E.g. Brand, open date...",
                singleLine = false,
                modifier = Modifier.heightIn(min = 100.dp)
            )

            Button(
                onClick = {
                    onSave(
                        foodItem.copy(
                            name = name,
                            quantity = quantity.toDoubleOrNull() ?: 0.0,
                            itemUnit = selectedUnit,
                            expiryDate = expiryDate,
                            purchaseDate = purchaseDate,
                            location = location,
                            notes = notes
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Add to Pantry", fontWeight = FontWeight.Bold)
            }
        }
    }

    when (activeDatePicker) {
        ActiveDatePicker.PURCHASE -> {
            AppDatePickerDialog(
                initialDateMillis = purchaseDate,
                title = "Select Purchase Date",
                onDateSelected = { purchaseDate = it },
                onDismiss = { activeDatePicker = ActiveDatePicker.NONE }
            )
        }

        ActiveDatePicker.EXPIRY -> {
            AppDatePickerDialog(
                initialDateMillis = if (expiryDate == -1L) null else expiryDate,
                title = "Select Expiry Date",
                onDateSelected = { expiryDate = it },
                onDismiss = { activeDatePicker = ActiveDatePicker.NONE }
            )
        }

        ActiveDatePicker.NONE -> {}
    }
}

@Preview
@Composable
fun ScanResultBottomSheetPreview(){
    AlgidyTheme {
        ScanResultBottomSheet(
            foodItem = FoodItem(),
            onSave = {},
            onDismiss = {},
            modifier = Modifier
        )
    }
}
