package com.nhuhuy.algidy.feature.inventory.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.component.toUiText
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import com.nhuhuy.algidy.toReadableText

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailBottomSheetContent(
    uiState: DetailBottomSheetUiState,
    onEditClick: () -> Unit,
    onConsumedClick: () -> Unit,
    onWastedClick: () -> Unit
) {
    val localShape = LocalAlgidyShapes.current
    val localSpacing = LocalAlgidySpacing.current
    val foodItem = uiState.foodItem

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        DetailTopBar(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(localSpacing.large))

        DetailMainContainer(
            modifier = Modifier.fillMaxWidth(),
            onConsumeClick = onConsumedClick,
            foodName = foodItem.name,
            foodImageUri = foodItem.imageUri,
            remainingDays = foodItem.remainDays,
            category = foodItem.categoryUiModel.toUiText(),
            storageLocation = stringResource(foodItem.location.toStringRes())
        )

        Spacer(modifier = Modifier.height(localSpacing.large))

        if (foodItem.note.isNotBlank()) {
            DetailNote(
                modifier = Modifier.fillMaxWidth(),
                note = foodItem.note
            )
            Spacer(modifier = Modifier.height(localSpacing.large))
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(localSpacing.medium)
        ) {
            DetailInfoCard(
                modifier = Modifier.weight(1f),
                title = "Purchase Date",
                text = foodItem.purchaseDate.toReadableText(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )

            DetailInfoCard(
                modifier = Modifier.weight(1f),
                title = "Expired Date",
                text = foodItem.expiryDate.toReadableText(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(localSpacing.extraExtraLarge))

        Row(
            modifier = Modifier.align(alignment = Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(localSpacing.large)
        ) {
            TextButton(
                onClick = onWastedClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(
                    text = "Waste Food",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = onEditClick,
                shape = localShape.extraExtraLarge,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = "Edit Food",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun DetailInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    val localShape = LocalAlgidyShapes.current
    val localSpacing = LocalAlgidySpacing.current
    Surface(
        shape = localShape.large,
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(localSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(localSpacing.small)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}