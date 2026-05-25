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
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.toBackgroundColor
import com.nhuhuy.algidy.core.presentation.utils.toContentColor
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import kotlin.math.abs

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailHeroCard(
    item: FoodItem,
    modifier: Modifier = Modifier
) {
    val freshnessProgress = item.calculateFreshnessProgress()
    val freshnessStatus = item.getFreshnessStatus()
    val remainingDays = item.getRemainingDays()

    val remainingDaysText = when {
        remainingDays == -1 -> stringResource(R.string.freshness_no_expiry)
        remainingDays < 0 -> stringResource(R.string.freshness_expired, abs(remainingDays))
        remainingDays == 0 -> stringResource(R.string.freshness_expires_today)
        remainingDays == 1 -> stringResource(R.string.freshness_one_day_left)
        remainingDays < 30 -> stringResource(R.string.freshness_days_left, remainingDays)
        else -> stringResource(R.string.freshness_months_left, remainingDays / 30)
    }

    CardLayout(
        modifier = modifier,
        icon = Icons.AutoMirrored.Outlined.ShowChart,
        title = stringResource(R.string.detail_expiry_progress),
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(100.dp)
            ) {
                CircularWavyProgressIndicator(
                    progress = { freshnessProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = freshnessStatus.toBackgroundColor(),
                    trackColor = freshnessStatus.toBackgroundColor().copy(alpha = 0.15f),
                    amplitude = { 1f },
                    wavelength = 20.dp
                )

                Text(
                    text = "${(freshnessProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = freshnessStatus.toBackgroundColor()
                )
            }

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PulsingCountdownText(
                    expiryDate = item.expiryDate,
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
                        text = remainingDaysText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (freshnessStatus == Freshness.EXPIRED)
                            MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = freshnessStatus.toBackgroundColor(),
                    shape = CircleShape,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = stringResource(freshnessStatus.toStringRes()),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = freshnessStatus.toContentColor(),
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
