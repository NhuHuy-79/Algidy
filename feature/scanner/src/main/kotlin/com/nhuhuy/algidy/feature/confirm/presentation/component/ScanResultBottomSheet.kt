package com.nhuhuy.algidy.feature.confirm.presentation.component

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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.ItemUnit
import com.nhuhuy.algidy.core.model.StorageLocation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    var showPurchaseDatePicker by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(foodItem.location) }
    var notes by remember { mutableStateOf(foodItem.notes) }

    var showDatePicker by remember { mutableStateOf(false) }
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

            // 1. Tên thực phẩm (Nhận từ API)
            AppTextField(
                value = name,
                onValueChange = { name = it },
                label = "Food Name",
                placeholder = "Enter food name..."
            )

            // 2. Hàng ngang: Số lượng và Đơn vị
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

                // Dropdown cho Đơn vị sử dụng AppTextField
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

            // 3. Ngày hết hạn (Sử dụng DatePicker)
            Box(modifier = Modifier.fillMaxWidth()) {
                AppTextField(
                    value = if (expiryDate == -1L) "Select Expiry Date" else dateFormatter.format(
                        Date(expiryDate)
                    ),
                    onValueChange = {},
                    label = "Expiry Date",
                    leadingIcon = Icons.Rounded.CalendarToday,
                    readOnly = true
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                )
            }

            // 4. Vị trí bảo quản (Sử dụng Segmented Button như bạn đã làm ở màn Edit)
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

            // 5. Ghi chú thêm
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

    // --- Trường Purchase Date ---
    Box(modifier = Modifier.fillMaxWidth()) {
        AppTextField(
            value = dateFormatter.format(Date(purchaseDate)),
            onValueChange = {},
            label = "Purchase Date",
            leadingIcon = Icons.Rounded.CalendarToday, // Bạn có thể đổi sang biểu tượng ShoppingCart nếu muốn khác biệt
            readOnly = true
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showPurchaseDatePicker = true }
        )
    }

// Logic hiển thị DatePicker cho Purchase Date
    if (showPurchaseDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = purchaseDate
        )
        DatePickerDialog(
            onDismissRequest = { showPurchaseDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    purchaseDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    showPurchaseDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showPurchaseDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Logic hiển thị DatePickerDialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (expiryDate == -1L) System.currentTimeMillis() else expiryDate
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    expiryDate = datePickerState.selectedDateMillis ?: -1L
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