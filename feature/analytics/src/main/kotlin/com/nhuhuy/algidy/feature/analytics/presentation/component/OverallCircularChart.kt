package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.CircularChartData


@Composable
fun OverallCircularChart(
    modifier: Modifier = Modifier,
    value: Float = 0.75f,
    containerColor: Color,
    contentColor: Color
) {
    var targetValue by rememberSaveable { mutableFloatStateOf(0f) }
    val animatedValue by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        )
    )

    LaunchedEffect(value) {
        targetValue = value
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(color = Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = contentColor,
            progress = { animatedValue },
            trackColor = containerColor,
            strokeWidth = 36.dp,
            strokeCap = StrokeCap.Round
        )

        Text(
            text = "${(animatedValue * 100f).toInt()}%",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Black
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun OverallChartLabel(
    modifier: Modifier = Modifier,
    selected: Boolean,
    icon: ImageVector,
    @StringRes label: Int,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    count: Int
) {

    val animatedDp by animateDpAsState(
        targetValue = if (selected) 24.dp else 8.dp,
        label = "animate dp"
    )

    val animatedBackground by animateColorAsState(
        targetValue = if (selected) backgroundColor else MaterialTheme.colorScheme.surfaceVariant
    )

    val animatedContent by animateColorAsState(
        targetValue = if (selected) contentColor else MaterialTheme.colorScheme.onSurfaceVariant
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(size = animatedDp))
            .background(color = animatedBackground)
            .clickable(enabled = true, onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = animatedContent
        )

        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Black
            ),
            modifier = Modifier
                .weight(1f)
                .basicMarquee(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = animatedContent
        )

        AnimatedVisibility(visible = selected) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Black
                ),
                maxLines = 1
            )
        }
    }
}

@StringRes
fun CircularChartData.toStringRes(): Int {
    return when (this) {
        CircularChartData.OTHERS -> R.string.analytics_card_others
        CircularChartData.CONSUMED -> R.string.analytics_card_consumed
        CircularChartData.WASTED -> R.string.analytics_card_wasted
    }
}

@Composable
fun CircularChartData.toImageVector(): ImageVector {
    return when (this) {
        CircularChartData.OTHERS -> ImageVector.vectorResource(com.nhuhuy.algidy.core.designsystem.R.drawable.ic_storage)
        CircularChartData.CONSUMED -> Icons.Rounded.DeleteForever
        CircularChartData.WASTED -> Icons.Rounded.TaskAlt
    }
}

@Composable
fun CircularChartData.toContainerColor(): Color {
    return when (this) {
        CircularChartData.OTHERS -> MaterialTheme.colorScheme.secondaryContainer
        CircularChartData.CONSUMED -> AlgidyTheme.extendedColors.consumedContainer
        CircularChartData.WASTED -> AlgidyTheme.extendedColors.wastedContainer
    }
}

@Composable
fun CircularChartData.toContentColor(): Color {
    return when (this) {
        CircularChartData.OTHERS -> MaterialTheme.colorScheme.onSecondaryContainer
        CircularChartData.CONSUMED -> AlgidyTheme.extendedColors.onConsumedContainer
        CircularChartData.WASTED -> AlgidyTheme.extendedColors.onWastedContainer
    }
}
