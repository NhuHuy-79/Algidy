package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductStatsCard(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    icon: ImageVector,
    title: String,
    description: String,
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier.clip(
            shape = RoundedCornerShape(16.dp)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
            )

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )

            Text(
                text = description,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black
                )
            )
        }
    }
}