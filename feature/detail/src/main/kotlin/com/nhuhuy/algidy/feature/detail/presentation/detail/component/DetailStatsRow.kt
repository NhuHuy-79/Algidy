package com.nhuhuy.algidy.feature.detail.presentation.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Kitchen
import androidx.compose.material.icons.rounded.Scale
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.formatMillisToDate

@Composable
fun DetailStatsRow(
    item: FoodItem,
    modifier: Modifier = Modifier
) {
    CardLayout(
        icon = Icons.Rounded.Fastfood,
        title = stringResource(R.string.detail_food_stats),
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatItem(
                icon = Icons.Rounded.Scale,
                label = stringResource(R.string.confirm_label_quantity),
                value = "${item.quantity} ${item.itemUnit.name.lowercase()}",
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )

            StatItem(
                icon = when (item.location) {
                    StorageLocation.FRIDGE -> Icons.Rounded.AcUnit
                    StorageLocation.FREEZER -> Icons.Rounded.Kitchen
                    else -> Icons.Rounded.Inventory2
                },
                label = stringResource(R.string.confirm_label_location),
                value = item.location.name.capitalize(),
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )

            StatItem(
                icon = Icons.Rounded.CalendarToday,
                label = stringResource(R.string.detail_bought),
                value = item.purchaseDate.formatMillisToDate(),
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                minLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                minLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DetailComponentsPreview() {
    val oneDayInMs = 24 * 60 * 60 * 1000L
    val fakeItem = FoodItem(
        id = "1",
        name = "Premium Wagyu Beef",
        location = StorageLocation.FRIDGE,
        purchaseDate = System.currentTimeMillis() - (2 * oneDayInMs),
        expiryDate = System.currentTimeMillis() + (5 * oneDayInMs),
        quantity = 1.2,
        itemUnit = ItemUnit.KG,
        isFavorite = true,
        notes = "Mua tại hàng chú Bảy"
    )

    AlgidyTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.detail_item_details),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            DetailHeroCard(item = fakeItem)

            DetailStatsRow(item = fakeItem)
        }
    }
}
