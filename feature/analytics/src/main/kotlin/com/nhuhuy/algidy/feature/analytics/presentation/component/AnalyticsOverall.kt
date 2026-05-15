package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.R
import com.nhuhuy.algidy.core.designsystem.component.CardLayout

@Composable
fun AnalyticsOverall(
    consumePercent: Float,
    wastedPercent: Float,
    modifier: Modifier = Modifier,
) {
    val cWeight = consumePercent.coerceAtLeast(0.05f)
    val wWeight = wastedPercent.coerceAtLeast(0.05f)
    val isConsumeLarger = consumePercent >= wastedPercent

    CardLayout(
        modifier = modifier,
        icon = ImageVector.vectorResource(R.drawable.ic_grocery),
        title = "Overall"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (isConsumeLarger) {
                ConsumeContent(
                    modifier = Modifier.weight(cWeight),
                    value = consumePercent,
                    position = OverallPosition.PRIMARY,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
                ConsumeContent(
                    modifier = Modifier.weight(wWeight),
                    value = wastedPercent,
                    position = OverallPosition.SECONDARY,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            } else {
                ConsumeContent(
                    modifier = Modifier.weight(wWeight),
                    value = wastedPercent,
                    position = OverallPosition.PRIMARY,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
                ConsumeContent(
                    modifier = Modifier.weight(cWeight),
                    value = consumePercent,
                    position = OverallPosition.SECONDARY,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun ConsumeContent(
    modifier: Modifier,
    value: Float,
    position: OverallPosition,
    containerColor: Color,
    contentColor: Color
) {
    val shape = remember(position) {
        when (position) {
            OverallPosition.PRIMARY -> RoundedCornerShape(
                topStart = 24.dp,
                bottomStart = 24.dp,
                topEnd = 8.dp,
                bottomEnd = 8.dp
            )

            OverallPosition.SECONDARY -> RoundedCornerShape(
                topStart = 8.dp,
                bottomStart = 8.dp,
                topEnd = 24.dp,
                bottomEnd = 24.dp
            )
        }
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${(value * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

enum class OverallPosition {
    PRIMARY, SECONDARY
}