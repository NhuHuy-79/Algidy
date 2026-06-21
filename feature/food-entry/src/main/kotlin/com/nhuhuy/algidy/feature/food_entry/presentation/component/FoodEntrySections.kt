package com.nhuhuy.algidy.feature.food_entry.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppFilterButton
import com.nhuhuy.algidy.core.designsystem.component.AppTextField
import com.nhuhuy.algidy.core.designsystem.component.FoodImageCard
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FoodImageSection(
    imageUri: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(160.dp)
            .clip(
                shape = MaterialShapes.Cookie12Sided.toShape()
            )
    ) {
        FoodImageCard(imageUri = imageUri, modifier = Modifier.fillMaxSize())
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

        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = StorageLocation.entries,
            ) { location ->
                AppFilterButton(
                    selected = selectedLocation == location,
                    label = stringResource(location.toStringRes()),
                    onClick = {
                        onLocationChange(location)
                    }
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
