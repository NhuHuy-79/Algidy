package com.nhuhuy.algidy.feature.food_entry.presentation.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import com.nhuhuy.algidy.toReadableText

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpiryDateField(
    modifier: Modifier = Modifier,
    itemPosition: ItemPosition,
    expiryDate: Long?,
    errorMessage: String? = null,
    onEditClick: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val algidyIcon = AlgidyIcons.FoodEntry
    LocalAlgidyShapes.current
    ListItem(
        shapes = ListItemDefaults.shapes(
            shape = itemPosition.toVerticalSegmentedShape()
        ),
        modifier = modifier,
        colors = ListItemDefaults.colors(
            containerColor = scheme.surface,
            headlineColor = scheme.onSurface,
            overlineColor = scheme.onSurface,
        ),
        onClick = onEditClick,
        leadingContent = {
            AppIcon(
                tint = scheme.onSecondaryContainer,
                iconProvider = algidyIcon.ExpiryDate,
            )
        },
        trailingContent = {
            FilledTonalIconButton(
                onClick = onEditClick,
                shape = CircleShape,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = scheme.primary,
                    contentColor = scheme.onPrimary
                )
            ) {
                AppIcon(
                    iconProvider = AlgidyIcons.FoodEntry.EditFood,
                )
            }
        },
        supportingContent = {
            errorMessage?.let {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        verticalAlignment = Alignment.CenterVertically,
        overlineContent = {
            Text(
                text = stringResource(R.string.food_entry_expiry_date),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    ) {
        Text(
            text = expiryDate?.toReadableText()
                ?: stringResource(R.string.food_entry_expiry_date_placeholder),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}