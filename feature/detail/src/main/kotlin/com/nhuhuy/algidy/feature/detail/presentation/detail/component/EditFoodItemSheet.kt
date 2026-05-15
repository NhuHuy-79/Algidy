package com.nhuhuy.algidy.feature.detail.presentation.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.component.FoodEntryForm
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryError
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryUiState
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFoodBottomSheet(
    editEntry: FoodEntryUiState,
    errorState: FoodEntryError,
    onAction: (DetailAction) -> Unit,
) {
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

            FoodEntryForm(
                entryState = editEntry,
                errorState = errorState,
                onAction = { entryAction ->
                    onAction(DetailAction.EditEntryAction.OnEntryAction(entryAction))
                }
            )

            Button(
                onClick = { onAction(DetailAction.EditEntryAction.OnSave) },
                enabled = errorState.isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Save Changes", fontWeight = FontWeight.Bold)
            }
        }
    }
}
