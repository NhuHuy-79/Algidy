package com.nhuhuy.algidy.feature.inventory.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.component.FoodImage
import com.nhuhuy.algidy.feature.inventory.presentation.model.readableRemainDays

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailMainContainer(
    modifier: Modifier = Modifier,
    onConsumeClick: () -> Unit,
    foodImageUri: String? = null,
    foodName: String,
    category: String,
    storageLocation: String,
    remainingDays: Int,
) {
    ListItem(
        onClick = {},
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        shapes = ListItemDefaults.shapes(
            shape = RoundedCornerShape(48.dp)
        ),
        leadingContent = {
            FoodImage(
                modifier = Modifier
                    .size(72.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                imageUrl = foodImageUri,
            )
        },
        overlineContent = {
            Text(
                text = "$category - $storageLocation",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
        },
        trailingContent = {
            FilledIconButton(
                onClick = onConsumeClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                AppIcon(iconProvider = AlgidyIcons.ConsumeFood)
            }
        },
        content = {
            Text(
                text = foodName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        },
        supportingContent = {
            Text(
                text = readableRemainDays(remainingDays),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    )
}

@Composable
fun DetailNote(
    modifier: Modifier = Modifier,
    note: String,
) {
    val localShape = LocalAlgidyShapes.current
    val localSpacing = LocalAlgidySpacing.current

    Card(
        modifier = modifier,
        shape = localShape.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(localSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(localSpacing.small)
        ) {
            Text(
                text = "Your Note",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = note,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

