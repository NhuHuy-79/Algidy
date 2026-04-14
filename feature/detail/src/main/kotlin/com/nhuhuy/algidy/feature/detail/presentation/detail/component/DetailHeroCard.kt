package com.nhuhuy.algidy.feature.detail.presentation.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.Freshness
import com.nhuhuy.algidy.core.model.ItemUnit
import com.nhuhuy.algidy.core.model.StorageLocation

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailHeroCard(
    item: FoodItem,
    modifier: Modifier = Modifier
) {
    val freshnessProgress = item.calculateFreshnessProgress()
    val freshnessStatus = item.getFreshnessStatus()

    val statusColor = when (freshnessStatus) {
        Freshness.EXPIRED -> MaterialTheme.colorScheme.error
        Freshness.URGENT -> MaterialTheme.colorScheme.tertiary
        Freshness.WARNING -> MaterialTheme.colorScheme.secondary
        Freshness.FRESH -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(100.dp)
            ) {
                CircularWavyProgressIndicator(
                    progress = { freshnessProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = statusColor,
                    trackColor = statusColor.copy(alpha = 0.15f),
                    amplitude = { 1f },
                    wavelength = 20.dp
                )

                Text(
                    text = "${(freshnessProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (freshnessStatus == Freshness.EXPIRED)
                            MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = item.getRemainingDaysText(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (freshnessStatus == Freshness.EXPIRED)
                            MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = statusColor.copy(alpha = 0.12f),
                    shape = CircleShape,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = freshnessStatus.name.lowercase().capitalize(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HeroCardPreview() {
    val oneDayInMs = 24 * 60 * 60 * 1000L
    AlgidyTheme {
        DetailHeroCard(
            item = FoodItem(
                id = "1",
                name = "Premium Wagyu Beef",
                location = StorageLocation.FRIDGE,
                purchaseDate = System.currentTimeMillis(),
                expiryDate = System.currentTimeMillis() + (3 * oneDayInMs),
                quantity = 1.2,
                itemUnit = ItemUnit.KG,
                isFavorite = true,
                notes = "Mua tại hàng chú Bảy"
            )
        )
    }
}