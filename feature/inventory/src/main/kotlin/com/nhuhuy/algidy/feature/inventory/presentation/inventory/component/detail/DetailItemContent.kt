package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toRoundedCornerShape

@Composable
fun DetailListItem(
    itemPosition: ItemPosition,
    icon: ImageVector,
    title: String,
    content: String,
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clip(itemPosition.toRoundedCornerShape(large = 24.dp, small = 4.dp)),
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        headlineContent = {
            Text(
                text = title,
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            Text(
                text = content
            )
        }
    )
}