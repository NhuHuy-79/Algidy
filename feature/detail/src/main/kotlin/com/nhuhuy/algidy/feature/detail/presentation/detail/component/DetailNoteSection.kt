package com.nhuhuy.algidy.feature.detail.presentation.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.model.food.FoodItem

@Composable
fun DetailNoteSection(
    item: FoodItem,
    modifier: Modifier = Modifier
) {
    CardLayout(
        modifier = modifier,
        icon = Icons.Rounded.EditNote,
        title = "Personal Notes",
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .defaultMinSize(minHeight = 100.dp),
            contentAlignment = Alignment.Center
        ) {
            if (item.notes.isNotBlank()) {
                Text(
                    text = item.notes,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 26.sp, // Tăng line height để đọc dễ hơn
                    fontWeight = FontWeight.Medium
                )
            } else {
                // Empty state tinh tế hơn
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No notes for this item yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}
