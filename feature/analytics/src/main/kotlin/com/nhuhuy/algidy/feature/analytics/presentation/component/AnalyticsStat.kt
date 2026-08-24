package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing

@Composable
fun AnalyticsState(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    containerColor: Color,
    contentColor: Color
) {
    val localShape = LocalAlgidyShapes.current
    val localSpacing = LocalAlgidySpacing.current
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor,
        shape = localShape.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(localSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(localSpacing.small)
        ) {
            Text(
                text = content,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold,
                color = contentColor
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = contentColor.copy(alpha = 0.8f)
            )
        }
    }
}