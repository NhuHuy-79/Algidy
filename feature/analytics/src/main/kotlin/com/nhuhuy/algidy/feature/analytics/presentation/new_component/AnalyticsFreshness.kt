package com.nhuhuy.algidy.feature.analytics.presentation.new_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing

@Composable
fun AnalyticsFreshnessChart(
    modifier: Modifier = Modifier,
) {
    val localSpacing = LocalAlgidySpacing.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(localSpacing.small)
    ) {
        repeat(7) {
            FreshnessBarWithDays(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
private fun FreshnessBarWithDays(
    modifier: Modifier = Modifier,
    label: String = "M"
) {
    val localSpacing = LocalAlgidySpacing.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(localSpacing.extraSmall)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )

        FreshnessBar(
            modifier = Modifier.fillMaxWidth(),
            freshnessPercent = 0.8f,
            urgentPercent = 0.1f,
            expiredPercent = 0.1f,
            warningPercent = 0.1f
        )
    }
}

@Composable
private fun FreshnessBar(
    modifier: Modifier,
    freshnessPercent: Float,
    urgentPercent: Float,
    expiredPercent: Float,
    warningPercent: Float,
) {
    LocalAlgidySpacing.current
    val localShape = LocalAlgidyShapes.current
    val extendColors = AlgidyTheme.extendedColors
    Row(
        modifier = modifier
            .clip(shape = localShape.medium)
            .height(24.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(freshnessPercent)
                .fillMaxHeight()
                .background(color = extendColors.fresh),
        )

        Box(
            modifier = Modifier
                .weight(urgentPercent)
                .fillMaxHeight()
                .background(color = extendColors.notice)
        )

        Box(
            modifier = Modifier
                .weight(warningPercent)
                .fillMaxHeight()
                .background(color = extendColors.warning)
        )

        Box(
            modifier = Modifier
                .weight(expiredPercent)
                .fillMaxHeight()
                .background(color = extendColors.expired)
        )
    }
}