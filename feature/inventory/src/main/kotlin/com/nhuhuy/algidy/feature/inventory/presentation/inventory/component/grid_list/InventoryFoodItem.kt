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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.FoodImageCard
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.presentation.utils.toBackgroundColor
import com.nhuhuy.algidy.core.presentation.utils.toContentColor
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.model.readableRemainDays

@Composable
fun InventoryFoodGridItem(
    modifier: Modifier = Modifier,
    item: FoodUiModel,
    isSelected: Boolean = false,
) {
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
            FoodImageCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                imageUri = item.imageUri,
            )

            ExpiryLabel(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart),
                freshness = item.freshness,
                remainingDays = item.remainDays
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
            AppIcon(
                iconProvider = AlgidyIcons.Inventory.RemainDays,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = readableRemainDays(remainingDays),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 2
            )
        }
    }
}
