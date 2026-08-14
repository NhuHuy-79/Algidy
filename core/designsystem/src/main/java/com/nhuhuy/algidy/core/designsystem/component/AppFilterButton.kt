package com.nhuhuy.algidy.core.designsystem.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppFilterButton(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    label: String,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    activeContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    activeContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    val animatedDpValue by animateDpAsState(
        targetValue = if (selected) 28.dp else 12.dp,
        label = "animated_dp"
    )

    Surface(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        color = if (selected) activeContainerColor else disabledContainerColor,
        contentColor = if (selected) activeContentColor else disabledContentColor,
        shape = RoundedCornerShape(animatedDpValue)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingContent?.invoke()
            Text(
                text = label,
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (selected) activeContentColor else disabledContentColor
            )
        }
    }

}