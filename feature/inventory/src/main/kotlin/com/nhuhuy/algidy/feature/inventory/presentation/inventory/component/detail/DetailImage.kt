package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.nhuhuy.algidy.formatMillisToDate
import kotlin.math.abs

@Composable
fun DetailImage(
    categoryUiModel: CategoryUiModel,
    foodItem: FoodItem,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailImageWithCategory(
                imageUri = foodItem.imageUri,
                name = foodItem.name,
                location = foodItem.location,
                categoryUiModel = categoryUiModel,
                onEditClick = onEditClick
            )
            DetailDateContent(
                modifier = Modifier.fillMaxWidth(),
                purchaseDate = foodItem.purchaseDate,
                expiryDate = foodItem.expiryDate
            )

            HorizontalDivider(thickness = 2.dp)

            if (foodItem.notes.isNotBlank()) {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.bodyLarge,
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

        }
    }


}

@Composable
fun DetailImageWithCategory(
    imageUri: String?,
    name: String,
    location: StorageLocation,
    categoryUiModel: CategoryUiModel,
    onEditClick: () -> Unit

) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FoodImageCard(
            imageUri = imageUri,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)

        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = name,
                maxLines = 2,
                style = MaterialTheme.typography.headlineSmall
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = categoryUiModel.toUiText(),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Black
                    ),
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )

                Text(
                    text = stringResource(location.toStringRes()),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )

            }
        }

        Spacer(modifier = Modifier.weight(1f))

        FilledTonalIconButton(
            onClick = onEditClick
        ) {
            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = null
            )
        }
    }
}

@Composable
fun DetailDateContent(
    modifier: Modifier,
    purchaseDate: Long,
    expiryDate: Long,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Purchase Date",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = purchaseDate.formatMillisToDate(),
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Expiry Date",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = expiryDate.formatMillisToDate(),
                style = MaterialTheme.typography.headlineSmall
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