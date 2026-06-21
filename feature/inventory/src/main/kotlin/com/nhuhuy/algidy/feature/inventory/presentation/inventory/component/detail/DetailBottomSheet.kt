package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBottomSheet(
    foodItem: FoodItem,
    categoryUiModel: CategoryUiModel,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit = {},
    onWastedClick: () -> Unit,
    onConsumedClick: () -> Unit,
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailImage(
                categoryUiModel = categoryUiModel,
                foodItem = foodItem,
                onEditClick = onEditClick
            )

            DetailToolbars(
                onMarkWasted = onWastedClick,
                onMarkConsumed = onConsumedClick
            )
        }
    }
}