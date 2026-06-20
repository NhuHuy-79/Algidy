package com.nhuhuy.algidy.feature.detail.presentation.detail.component


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.FoodImageCard
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.toUiText
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.utils.toBackgroundColor
import com.nhuhuy.algidy.core.presentation.utils.toContentContainerColor
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import kotlin.math.abs

@Composable
fun DetailImage(
    categoryUiModel: CategoryUiModel,
    foodItem: FoodItem,
    modifier: Modifier = Modifier,
) {
    var showExpiry by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailImageWithCategory(
                showExpiryView = showExpiry,
                imageUri = foodItem.imageUri,
                name = foodItem.name,
                location = foodItem.location,
                categoryUiModel = categoryUiModel,
                onExpiryView = { showExpiry = !showExpiry }
            )

            DetailFoodProgress(
                visible = showExpiry,
                freshness = foodItem.getFreshnessStatus(),
                progress = foodItem.calculateFreshnessProgress(),
                remainingDays = foodItem.getRemainingDays(),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }


}

@Composable
fun DetailImageWithCategory(
    showExpiryView: Boolean,
    imageUri: String?,
    name: String,
    location: StorageLocation,
    categoryUiModel: CategoryUiModel,
    onExpiryView: () -> Unit

) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FoodImageCard(
            imageUri = imageUri,
            modifier = Modifier.size(56.dp)
        )

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = name,
                maxLines = 1,
                modifier = Modifier.basicMarquee(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = categoryUiModel.toUiText(),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Text(
                    text = stringResource(location.toStringRes()),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        FilledTonalIconButton(
            onClick = onExpiryView,
            modifier = Modifier.size(24.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = if (showExpiryView) Icons.Rounded.ArrowUpward else Icons.Rounded.Timeline,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailFoodProgress(
    progress: Float,
    remainingDays: Int,
    visible: Boolean,
    modifier: Modifier = Modifier,
    freshness: Freshness,
) {
    val remainingDaysText = when {
        remainingDays == -1 -> stringResource(R.string.freshness_no_expiry)
        remainingDays < 0 -> stringResource(R.string.freshness_expired, abs(remainingDays))
        remainingDays == 0 -> stringResource(R.string.freshness_expires_today)
        remainingDays == 1 -> stringResource(R.string.freshness_one_day_left)
        remainingDays < 30 -> stringResource(R.string.freshness_days_left, remainingDays)
        else -> stringResource(R.string.freshness_months_left, remainingDays / 30)
    }
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(freshness.toStringRes()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = freshness.toContentContainerColor(),
                )

                Text(
                    text = remainingDaysText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = freshness.toContentContainerColor()
                )
            }

            LinearWavyProgressIndicator(
                progress = { progress },
                trackColor = MaterialTheme.colorScheme.onSecondaryContainer,
                color = freshness.toBackgroundColor(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
        }
    }
}