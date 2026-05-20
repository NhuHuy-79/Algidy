package com.nhuhuy.algidy.feature.scanner.presentation.confirm

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.AppButton
import com.nhuhuy.algidy.core.designsystem.component.AppTextField
import com.nhuhuy.algidy.core.designsystem.component.FoodImageCard
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.PhotoPickerContainer
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.asString
import com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel.ConfirmUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmScreen(
    uiState: ConfirmUiState,
    onImageChange: (Uri?) -> Unit,
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

    BackHandler { onBackClick() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.confirm_title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
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
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                FoodImageCard(
                    imageUri = foodItem.imageUri,
                    modifier = Modifier.fillMaxWidth()
                )

                PhotoPickerContainer(
                    onImagePicked = onImageChange
                ) { launcher ->
                    FilledTonalIconButton(
                        onClick = launcher,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AddPhotoAlternate,
                            contentDescription = stringResource(R.string.detail_edit_image)
                        )
                    }
                }
            }

            // 1. Tên thực phẩm
            AppTextField(
                isError = uiState.errorState.isNameError,
                value = foodItem.name,
                onValueChange = onNameChange,
                errorMessage = uiState.errorState.nameValidation.asString(),
                label = stringResource(R.string.confirm_label_name),
                placeholder = stringResource(R.string.confirm_placeholder_name),
                leadingIcon = Icons.Rounded.Fastfood
            )

            // 2. Số lượng và Đơn vị
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppTextField(
                    modifier = Modifier.weight(0.6f),
                    isError = uiState.errorState.isQuantityError,
                    value = "${uiState.foodItem.quantity}",
                    onValueChange = onQuantityChange,
                    errorMessage = uiState.errorState.quantityValidation.asString(),
                    label = stringResource(R.string.confirm_label_quantity),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Box(modifier = Modifier.weight(0.4f)) {
                    AppTextField(
                        value = foodItem.itemUnit.name.lowercase().capitalize(),
                        onValueChange = {},
                        label = stringResource(R.string.confirm_label_unit),
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
                        label = stringResource(R.string.confirm_label_purchase_date),
                        isError = uiState.errorState.isPurchaseDateError,
                        errorMessage = uiState.errorState.purchaseDateValidation.asString(),
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
                        value = if (foodItem.expiryDate == -1L) stringResource(R.string.confirm_date_set)
                        else dateFormatter.format(Date(foodItem.expiryDate)),
                        errorMessage = uiState.errorState.expiryDateValidation.asString(),
                        onValueChange = {},
                        label = stringResource(R.string.confirm_label_expiry_date),
                        isError = uiState.errorState.isExpiryDateError,
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
                    text = stringResource(R.string.confirm_label_location),
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
                label = stringResource(R.string.confirm_label_notes),
                leadingIcon = Icons.Rounded.EditNote,
                placeholder = stringResource(R.string.confirm_placeholder_notes),
                singleLine = false,
                modifier = Modifier.heightIn(min = 100.dp)
            )

            // 6. Nút Save
            AppButton(
                enabled = uiState.errorState.valid,
                text = stringResource(R.string.confirm_btn_add),
                icon = Icons.Rounded.CheckCircleOutline,
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            )
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
            }) { Text(stringResource(R.string.action_ok)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
