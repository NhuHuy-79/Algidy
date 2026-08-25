package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemShapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.feature.analytics.domain.model.AnalyticsPeriod
import com.nhuhuy.algidy.feature.analytics.domain.model.getId

@Composable
fun AnalyticsFilterButton(
    currentPeriod: AnalyticsPeriod,
    modifier: Modifier = Modifier,
    onPeriodSelect: (period: AnalyticsPeriod) -> Unit
) {
    val localShape = LocalAlgidyShapes.current
    var expand by remember { mutableStateOf(false) }
    val algidyIcon = AlgidyIcons.Analytics

    Box(
        modifier = modifier,
    ) {
        TextButton(
            modifier = Modifier.height(48.dp),
            shape = localShape.extraLarge,
            onClick = { expand = !expand },
        ) {
            Text(
                text = stringResource(currentPeriod.getId()),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            AnimatedContent(
                targetState = expand
            ) { expand ->
                if (expand) AppIcon(iconProvider = algidyIcon.ClosePeriodFilter)
                else AppIcon(iconProvider = algidyIcon.OpenPeriodFilter)
            }
        }


        PeriodFilterDropdown(
            modifier = Modifier.align(Alignment.Center),
            onDismiss = { expand = false },
            expand = expand,
            currentPeriod = currentPeriod,
            onPeriodSelect = { period ->
                onPeriodSelect(period)
                expand = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PeriodFilterDropdown(
    onDismiss: () -> Unit,
    expand: Boolean,
    currentPeriod: AnalyticsPeriod,
    onPeriodSelect: (period: AnalyticsPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    val localShape = LocalAlgidyShapes.current
    DropdownMenu(
        onDismissRequest = onDismiss,
        modifier = modifier,
        expanded = expand,
        shape = localShape.large
    ) {
        AnalyticsPeriod.entries.forEach { period ->
            val selected = period == currentPeriod
            DropdownMenuItem(
                shapes = MenuItemShapes(
                    shape = localShape.medium,
                    selectedShape = localShape.large
                ),
                selected = selected,
                onClick = { onPeriodSelect(period) },
                text = {
                    Text(
                        text = stringResource(period.getId()),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    }
}