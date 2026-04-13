package com.nhuhuy.aldidy.feature.inventory.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Icecream
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.Freshness

@Composable
fun InventoryFoodCard(
    modifier: Modifier = Modifier,
    item: FoodItem = FoodItem(
        name = "Food"
    ),
    onClick: () -> Unit = {}
) {
    val freshness = item.getFreshnessStatus()
    when (freshness) {
        Freshness.EXPIRED -> Color(0xFFE57373)
        Freshness.URGENT -> Color(0xFFFFB74D)
        Freshness.WARNING -> Color(0xFFFFF176)
        Freshness.FRESH -> Color(0xFF81C784)
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    )
                    .size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Icecream,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Text(
                text = "Ice cream",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "2.4 Kg left",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AlgidyTheme {
        InventoryFoodCard()
    }

}