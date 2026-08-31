package com.nhuhuy.algidy.core.presentation.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PhoneFrame(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(0.462f)
            .shadow(
                elevation = 40.dp,
                shape = RoundedCornerShape(44.dp),
                spotColor = Color.Black.copy(alpha = 0.6f)
            )
            .clip(RoundedCornerShape(44.dp))
            .background(Color(0xFF1C1C1E))  // Device bezel
            .border(12.dp, Color(0xFF2C2C2E), RoundedCornerShape(44.dp))
            .padding(12.dp)
    ) {
        // Screen content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(32.dp))
                .background(color = backgroundColor)
        ) {
            content()
        }

        // Dynamic Island
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 14.dp)
                .size(width = 100.dp, height = 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black)
        )
    }
}