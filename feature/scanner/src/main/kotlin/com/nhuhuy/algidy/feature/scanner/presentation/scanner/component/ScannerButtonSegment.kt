package com.nhuhuy.algidy.feature.scanner.presentation.scanner.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.theme.LocalCameraColorScheme
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SelectImageButton(
    modifier: Modifier,
    onClick: () -> Unit,
    containerColor: Color = Color.White,
    contentColor: Color = Color.Gray
) {
    FilledTonalIconButton(
        modifier = modifier,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = contentColor,
            containerColor = containerColor
        ),
        shape = MaterialShapes.Square.toShape()
    ) {
        AppIcon(iconProvider = AlgidyIcons.Scanner.AddImage)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AutoScanButton(
    autoScanning: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit,
) {
    val localSpacing = LocalAlgidySpacing.current
    val cameraScheme = LocalCameraColorScheme.current
    Box(
        modifier = modifier
            .background(
                color = cameraScheme.primaryContainer,
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClick(!autoScanning) }
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = autoScanning
        ) { autoScanning ->
            if (autoScanning) {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(localSpacing.extraSmall),
                    color = cameraScheme.onPrimaryContainer
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(localSpacing.extraSmall)
                        .background(
                            color = cameraScheme.onPrimaryContainer,
                            shape = CircleShape
                        ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddManuallyBarcodeButton(
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    onClick: () -> Unit,
) {
    val cameraScheme = LocalCameraColorScheme.current
    FilledTonalIconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enable,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = cameraScheme.onSecondaryContainer,
            containerColor = cameraScheme.secondaryContainer
        ),
        shape = MaterialShapes.Square.toShape()
    ) {
        AppIcon(iconProvider = AlgidyIcons.Scanner.AddBarcode)
    }
}
