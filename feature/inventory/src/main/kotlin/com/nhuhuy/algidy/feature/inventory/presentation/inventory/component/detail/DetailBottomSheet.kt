package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.toReadableDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBottomSheet(
    foodItem: FoodItem,
    categoryUiModel: CategoryUiModel,
    onDismiss: () -> Unit,
    onWastedClick: () -> Unit,
    onConsumedClick: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailImage(
                categoryUiModel = categoryUiModel,
                foodItem = foodItem,
            )

            Text(
                text = "Dates",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            DetailListItem(
                itemPosition = ItemPosition.TOP,
                icon = Icons.Rounded.HourglassTop,
                title = "Purchase Date",
                content = foodItem.purchaseDate.toReadableDate()
            )
            DetailListItem(
                itemPosition = ItemPosition.BOTTOM,
                icon = Icons.Rounded.HourglassBottom,
                title = "Expiry Date",
                content = foodItem.expiryDate.toReadableDate()
            )

            if (foodItem.notes.isNotBlank()) {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.titleMedium
                )

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = foodItem.notes,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            }

            DetailToolbars(
                onMarkWasted = onWastedClick,
                onMarkConsumed = onConsumedClick
            )
        }
    }
}