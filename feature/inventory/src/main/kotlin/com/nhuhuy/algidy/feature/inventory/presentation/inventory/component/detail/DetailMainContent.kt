package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.FoodImageCard
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.toUiText
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.utils.toBackgroundContainerColor
import com.nhuhuy.algidy.core.presentation.utils.toContentContainerColor
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodCardUiModel
import com.nhuhuy.algidy.toReadableText
import kotlin.math.abs

@Composable
fun DetailMainContent(
    modifier: Modifier = Modifier,
    categoryUiModel: CategoryUiModel,
    foodItem: FoodCardUiModel,
    onEditClick: () -> Unit,
) {
    val spacing = LocalAlgidySpacing.current
    val shapes = LocalAlgidyShapes.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(spacing.large)
        ) {

            DetailHeroSection(
                imageUri = foodItem.imageUri,
                name = foodItem.name,
                remainingDays = foodItem.remainDays,
                freshness = foodItem.freshness,
                purchaseDateText = foodItem.purchaseDate.toReadableText(),
                expiryDateText = foodItem.expiryDate.toReadableText(),
                onEditClick = onEditClick
            )

            DetailInfoGrid(
                category = categoryUiModel.toUiText(),
                location = stringResource(foodItem.location.toStringRes())
            )

            if (foodItem.note.isNotBlank()) {
                DetailNoteSection(
                    note = foodItem.note
                )
            }
        }
    }
}

@Composable
private fun DetailHeroSection(
    imageUri: String?,
    name: String,
    remainingDays: Int,
    freshness: Freshness,
    purchaseDateText: String,
    expiryDateText: String,
    onEditClick: () -> Unit,
) {
    val remainingDaysText = when {
        remainingDays == -1 ->
            stringResource(R.string.freshness_no_expiry)

        remainingDays < 0 ->
            stringResource(
                R.string.freshness_expired,
                abs(remainingDays)
            )

        remainingDays == 0 ->
            stringResource(R.string.freshness_expires_today)

        remainingDays == 1 ->
            stringResource(R.string.freshness_one_day_left)

        remainingDays < 30 ->
            stringResource(
                R.string.freshness_days_left,
                remainingDays
            )

        else ->
            stringResource(
                R.string.freshness_months_left,
                remainingDays / 30
            )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            FoodImageCard(
                imageUri = imageUri,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(32.dp))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Surface(
                    color = freshness.toBackgroundContainerColor(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = remainingDaysText,
                        color = freshness.toContentContainerColor(),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )
                    )
                }

                Text(
                    text = "$purchaseDateText - $expiryDateText",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            IconButton(
                onClick = onEditClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun DetailInfoGrid(
    category: String,
    location: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        InfoCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.inventory_category),
            value = category
        )

        InfoCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.inventory_location),
            value = location
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DetailNoteSection(
    note: String
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHighest
    ) {

        Row {
            Text(
                text = note,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}