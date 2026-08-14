package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.FoodImage
import com.nhuhuy.algidy.core.presentation.utils.toBackgroundColor
import com.nhuhuy.algidy.core.presentation.utils.toContentColor
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import kotlin.math.abs

@Composable
fun InventoryFoodGridItem(
    modifier: Modifier = Modifier,
    item: FoodItem,
    isSelected: Boolean = false,
) {
    val freshness = item.getFreshnessStatus()
    val remainingDays = item.getRemainingDays()
    val localShape = LocalAlgidyShapes.current
    val localSpacing = LocalAlgidySpacing.current
    val scheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier,
        shape = localShape.large,
        colors = CardDefaults.cardColors(
            containerColor = scheme.surfaceContainer,
            contentColor = scheme.onSurface
        ),
        border = if (isSelected) BorderStroke(
            width = 4.dp,
            color = scheme.onPrimaryContainer
        ) else null
    ) {
        Box {
            FoodImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                imageUrl = item.imageUri,
            )

            ExpiryLabel(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart),
                freshness = freshness,
                remainingDays = remainingDays
            )
        }
        Spacer(modifier = Modifier.height(localSpacing.small))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(localSpacing.extraSmall)
        ) {
            Text(
                text = item.name,
                maxLines = 2,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Text(
                text = stringResource(item.location.toStringRes()),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
private fun ExpiryLabel(
    modifier: Modifier = Modifier,
    freshness: Freshness,
    remainingDays: Int,
) {
    val localShape = LocalAlgidyShapes.current
    val remainingDaysText = when {
        remainingDays == -1 -> stringResource(R.string.freshness_no_expiry)
        remainingDays < 0 -> stringResource(R.string.freshness_expired, abs(remainingDays))
        remainingDays == 0 -> stringResource(R.string.freshness_expires_today)
        remainingDays == 1 -> stringResource(R.string.freshness_one_day_left)
        remainingDays < 30 -> stringResource(R.string.freshness_days_left, remainingDays)
        else -> stringResource(R.string.freshness_months_left, remainingDays / 30)
    }

    Surface(
        color = freshness.toBackgroundColor(),
        shape = localShape.large,
        contentColor = freshness.toContentColor(),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Timer,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = remainingDaysText,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 2
            )
        }
    }
}
