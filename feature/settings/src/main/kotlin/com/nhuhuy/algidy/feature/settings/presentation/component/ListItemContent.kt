package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ListItemContent(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String,
    shape: Shape = RoundedCornerShape(16.dp),
    trailingContent: @Composable () -> Unit = {},
    bottomContent: @Composable () -> Unit,
) {
    ListItem(
        modifier = modifier,
        shapes = ListItemDefaults.shapes(shape = shape),
        onClick = {},
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            headlineColor = MaterialTheme.colorScheme.onSurface,
            leadingIconColor = MaterialTheme.colorScheme.onSurface
        ),
        leadingContent = {
            icon?.let {
                Icon(imageVector = it, contentDescription = title)
            }
        },
        supportingContent = bottomContent,
        trailingContent = trailingContent
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}