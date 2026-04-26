package com.nhuhuy.algidy.feature.confirm.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.AppButton
import com.nhuhuy.algidy.core.designsystem.component.AppTextField
import com.nhuhuy.algidy.core.model.ItemUnit
import com.nhuhuy.algidy.core.model.StorageLocation
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmScreen(
    uiState: ConfirmUiState,
    onNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onUnitSelected: (ItemUnit) -> Unit,
    onToggleUnitMenu: (Boolean) -> Unit,
    onTogglePurchaseDatePicker: (Boolean) -> Unit,
    onToggleExpiryDatePicker: (Boolean) -> Unit,
    onLocationChange: (StorageLocation) -> Unit,
    onNotesChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val foodItem = uiState.foodItem
    val dateFormatter = remember { SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Confirm Information",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // 1. Tên thực phẩm
            AppTextField(
                value = foodItem.name,
                onValueChange = onNameChange,
                label = "Food Name",
                placeholder = "Enter food name...",
                leadingIcon = Icons.Rounded.Fastfood
            )

            // 2. Số lượng và Đơn vị
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppTextField(
                    modifier = Modifier.weight(0.6f),
                    value = "${uiState.foodItem.quantity}",
                    onValueChange = onQuantityChange,
                    label = "Quantity",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Box(modifier = Modifier.weight(0.4f)) {
                    AppTextField(
                        value = foodItem.itemUnit.name.lowercase().capitalize(),
                        onValueChange = {},
                        label = "Unit",
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Rounded.ArrowDropDown, null) }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { onToggleUnitMenu(true) }
                    )
                    DropdownMenu(
                        expanded = uiState.expandedUnitMenu,
                        onDismissRequest = { onToggleUnitMenu(false) }
                    ) {
                        ItemUnit.entries.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit.name.lowercase().capitalize()) },
                                onClick = { onUnitSelected(unit) }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Purchase Date
                Box(modifier = Modifier.weight(1f)) {
                    AppTextField(
                        value = dateFormatter.format(Date(foodItem.purchaseDate)),
                        onValueChange = {},
                        label = "Purchase Date",
                        leadingIcon = Icons.Rounded.CalendarToday,
                        readOnly = true
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { onTogglePurchaseDatePicker(true) }
                    )
                }

                // Expiry Date
                Box(modifier = Modifier.weight(1f)) {
                    AppTextField(
                        value = if (foodItem.expiryDate == -1L) "Set Date"
                        else dateFormatter.format(Date(foodItem.expiryDate)),
                        onValueChange = {},
                        label = "Expiry Date",
                        leadingIcon = Icons.Rounded.EventAvailable,
                        readOnly = true
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { onToggleExpiryDatePicker(true) }
                    )
                }
            }

            // 4. Vị trí bảo quản
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Storage Location",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    StorageLocation.entries.forEachIndexed { index, loc ->
                        SegmentedButton(
                            selected = foodItem.location == loc,
                            onClick = { onLocationChange(loc) },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = StorageLocation.entries.size
                            ),
                            label = { Text(loc.name.lowercase().capitalize()) }
                        )
                    }
                }
            }

            // 5. Ghi chú thêm
            AppTextField(
                value = foodItem.notes,
                onValueChange = onNotesChange,
                label = "Notes",
                placeholder = "E.g. Brand, open date...",
                singleLine = false,
                modifier = Modifier.heightIn(min = 100.dp)
            )

            // 6. Nút Save
            AppButton(
                text = "Add to Pantry",
                icon = Icons.Rounded.CheckCircleOutline,
                onClick = onSaveClick
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDatePickerDialog(
    initialDate: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}