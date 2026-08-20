package com.nhuhuy.algidy.feature.scanner.presentation.scanner.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
    Box(
        modifier = modifier
            .size(72.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
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
                        .padding(2.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary,
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
    FilledTonalIconButton(
        enabled = enable,
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        AppIcon(iconProvider = AlgidyIcons.Scanner.AddBarcode)
    }
}
