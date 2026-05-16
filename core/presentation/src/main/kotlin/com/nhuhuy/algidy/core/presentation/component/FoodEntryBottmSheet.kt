package com.nhuhuy.algidy.core.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppButton
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryError
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodEntryBottomSheet(
    onDismiss: () -> Unit,
    onAddManually: () -> Unit,
    foodEntryState: FoodEntryUiState,
    foodEntryError: FoodEntryError,
    onEntryAction: (FoodEntryAction) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    ) {
        FoodEntryForm(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            entryState = foodEntryState,
            errorState = foodEntryError,
            onAction = onEntryAction
        )

        AppButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = foodEntryError.isValid,
            text = "Add Manually",
            icon = Icons.Rounded.Edit,
            onClick = onAddManually
        )
    }
}