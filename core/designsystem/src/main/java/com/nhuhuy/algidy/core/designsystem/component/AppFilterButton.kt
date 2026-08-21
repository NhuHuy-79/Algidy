package com.nhuhuy.algidy.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    activeContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    activeContentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        color = if (selected) activeContainerColor else disabledContainerColor,
        contentColor = if (selected) activeContentColor else disabledContentColor,
        shape = shape,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
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