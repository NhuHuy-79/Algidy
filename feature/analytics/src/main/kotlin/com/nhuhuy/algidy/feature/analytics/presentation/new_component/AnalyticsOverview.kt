package com.nhuhuy.algidy.feature.analytics.presentation.new_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing

@Composable
fun AnalyticsOverview(
    modifier: Modifier = Modifier,
    foodCount: Int,
    expiringFoodCount: Int,
    expiredFoodCount: Int
) {
    val localSpacing = LocalAlgidySpacing.current
    val localShape = LocalAlgidyShapes.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(localSpacing.extraSmall)
    ) {
        OverviewCard(
            modifier = Modifier.fillMaxWidth(),
            shape = localShape.extraLarge,
            supportingContent = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(localSpacing.medium)
                ) {
                    LabelText(
                        text = "$expiringFoodCount Expiring Soon",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        contentColor = MaterialTheme.colorScheme.secondaryContainer
                    )

                    LabelText(
                        text = "$expiredFoodCount Expired",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        contentColor = MaterialTheme.colorScheme.errorContainer
                    )
                }
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(localSpacing.medium)
            ) {
                Text(
                    text = "$foodCount",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = "Foods",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

        }
    }
}

@Composable
private fun OverviewCard(
    modifier: Modifier = Modifier,
    shape: Shape,
    supportingContent: @Composable () -> Unit,
    headlineContent: @Composable () -> Unit,
) {
    val localSpacing = LocalAlgidySpacing.current
    Surface(
        modifier = modifier,
        shape = shape,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(localSpacing.large),
            verticalArrangement = Arrangement.spacedBy(
                localSpacing.medium,
                alignment = Alignment.CenterVertically
            ),
        ) {
            headlineContent()
            supportingContent()
        }
    }
}

@Composable
private fun LabelText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    contentColor: Color,
) {
    val localShape = LocalAlgidyShapes.current
    Surface(
        modifier = modifier,
        shape = localShape.large,
        color = color,
        contentColor = contentColor
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}