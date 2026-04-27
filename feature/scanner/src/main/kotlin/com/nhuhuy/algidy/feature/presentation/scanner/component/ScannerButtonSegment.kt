package com.nhuhuy.algidy.feature.presentation.scanner.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CaptureButton(
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    contentColor: Color,
    onCapturePress: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(CircleShape)
            .border(
                color = contentColor,
                shape = CircleShape,
                width = 2.dp
            ),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize()
                .background(
                    color = contentColor,
                    shape = CircleShape
                )
                .clickable(enabled = enable, onClick = onCapturePress),
        )
    }
}

@Composable
fun SelectImageButton(
    modifier: Modifier,
    onClick: () -> Unit,
    containerColor: Color = Color.White,
    contentColor: Color = Color.Gray
){
    FilledTonalIconButton(
        modifier = modifier
            .size(56.dp),
        onClick = onClick,
        shape = CircleShape,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = contentColor,
            containerColor = containerColor
        )
    ) {
        Icon(
            imageVector = Icons.Outlined.AddPhotoAlternate,
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AutoScanButton(
    autoScanning: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit,
    enableContainerColor: Color = Color.White,
    disableContainerColor: Color = Color.Gray.copy(alpha = 0.3f),
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Indeterminate")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    val dynamicProgress by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ProgressLength"
    )

    val innerShapeCorner by animateDpAsState(
        targetValue = if (autoScanning) 4.dp else 16.dp,
        label = "ShapeMorph"
    )

    Box(
        modifier = modifier
            .size(56.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onClick(!autoScanning)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (autoScanning) {
            CircularWavyProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(rotation),
                progress = { dynamicProgress },
                color = enableContainerColor,
                trackColor = Color.Transparent,
                wavelength = 18.dp,
                waveSpeed = 10.dp,
            )
        } else {
            CircularWavyProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(),
                progress = { 1f },
                color = disableContainerColor,
                trackColor = Color.Transparent,
                wavelength = 15.dp,
                waveSpeed = 10.dp,
            )
        }

        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    color = if (autoScanning) enableContainerColor else disableContainerColor,
                    shape = RoundedCornerShape(size = innerShapeCorner)
                )
        )
    }
}