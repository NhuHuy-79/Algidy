package com.nhuhuy.algidy.feature.food_entry.presentation.component

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.AppTextField
import com.nhuhuy.algidy.core.designsystem.component.FoodImageCard
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.PhotoPickerContainer
import com.nhuhuy.algidy.core.presentation.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FoodImageSection(
    imageUri: String?,
    onImagePick: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(160.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
        ) {
            FoodImageCard(imageUri = imageUri)
        }

        PhotoPickerContainer(onImagePicked = { uri -> uri?.let { onImagePick(it) } }) { launcher ->
            FilledTonalIconButton(
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                onClick = launcher,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Filled.AddPhotoAlternate,
                    contentDescription = stringResource(R.string.detail_pick_photo)
                )
            }
        }
    }
}

@Composable
fun BasicInfoSection(
    name: String,
    onNameChange: (String) -> Unit,
    isNameError: Boolean,
    nameErrorMessage: String,
    modifier: Modifier = Modifier
) {
    AppTextField(
        value = name,
        onValueChange = onNameChange,
        label = stringResource(R.string.confirm_label_name),
        placeholder = stringResource(R.string.confirm_placeholder_name),
        isError = isNameError,
        errorMessage = nameErrorMessage,
        modifier = modifier
    )
}

@Composable
fun QuantityUnitSection(
    quantity: Double,
    itemUnit: ItemUnit,
    onQuantityChange: (Double) -> Unit,
    onUnitChange: (ItemUnit) -> Unit,
    isQuantityError: Boolean,
    quantityErrorMessage: String,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppTextField(
            modifier = Modifier.weight(1f),
            value = if (quantity == 0.0) "" else "$quantity",
            onValueChange = { text -> onQuantityChange(text.toDoubleOrNull() ?: 0.0) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = stringResource(R.string.confirm_label_quantity),
            isError = isQuantityError,
            errorMessage = quantityErrorMessage
        )

        Box(modifier = Modifier.weight(1f)) {
            AppTextField(
                value = itemUnit.name.capitalize(),
                onValueChange = {},
                label = stringResource(R.string.confirm_label_unit),
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            Box(modifier = Modifier
                .matchParentSize()
                .clickable { menuExpanded = true })
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                ItemUnit.entries.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit.name.capitalize()) },
                        onClick = {
                            onUnitChange(unit)
                            menuExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DateSection(
    purchaseDate: Long,
    expiryDate: Long,
    onPurchaseClick: () -> Unit,
    onExpiryClick: () -> Unit,
    isPurchaseError: Boolean,
    purchaseErrorMessage: String,
    isExpiryError: Boolean,
    expiryErrorMessage: String,
    modifier: Modifier = Modifier
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            AppTextField(
                value = dateFormatter.format(Date(purchaseDate)),
                onValueChange = {},
                label = stringResource(R.string.confirm_label_purchase_date),
                leadingIcon = Icons.Rounded.CalendarToday,
                readOnly = true,
                isError = isPurchaseError,
                errorMessage = purchaseErrorMessage,
                modifier = Modifier.fillMaxWidth()
            )
            Box(modifier = Modifier
                .matchParentSize()
                .clickable { onPurchaseClick() })
        }

        Box(modifier = Modifier.weight(1f)) {
            AppTextField(
                value = if (expiryDate == -1L) stringResource(R.string.confirm_date_set) else dateFormatter.format(Date(expiryDate)),
                onValueChange = {},
                label = stringResource(R.string.confirm_label_expiry_date),
                leadingIcon = Icons.Rounded.CalendarToday,
                readOnly = true,
                isError = isExpiryError,
                errorMessage = expiryErrorMessage,
                modifier = Modifier.fillMaxWidth()
            )
            Box(modifier = Modifier
                .matchParentSize()
                .clickable { onExpiryClick() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageSection(
    selectedLocation: StorageLocation,
    onLocationChange: (StorageLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.confirm_label_location),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            StorageLocation.entries.forEachIndexed { index, loc ->
                SegmentedButton(
                    selected = selectedLocation == loc,
                    onClick = { onLocationChange(loc) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = StorageLocation.entries.size),
                    label = { Text(loc.name.capitalize(), style = MaterialTheme.typography.bodySmall) }
                )
            }
        }
    }
}

@Composable
fun NotesSection(
    notes: String,
    onNoteChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppTextField(
        value = notes,
        onValueChange = onNoteChange,
        label = stringResource(R.string.confirm_label_notes),
        placeholder = stringResource(R.string.confirm_placeholder_notes),
        singleLine = false,
        modifier = modifier.fillMaxWidth()
    )
}
