package com.nhuhuy.algidy.feature.scanner.presentation.scanner.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.theme.LocalCameraColorScheme
import com.nhuhuy.algidy.core.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerTopBar(
    isFlashOn: Boolean,
    onCloseClick: () -> Unit,
    onFlashSwitch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cameraScheme = LocalCameraColorScheme.current
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = cameraScheme.background,
            titleContentColor = cameraScheme.foreground,
            navigationIconContentColor = cameraScheme.foreground
        ),
        title = {
            Text(
                text = stringResource(R.string.scanner_title_barcode),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.basicMarquee(),
                maxLines = 1,
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                AppIcon(iconProvider = AlgidyIcons.Close)
            }
        },
        actions = {
            FilledTonalIconButton(
                onClick = onFlashSwitch,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = cameraScheme.secondaryContainer,
                    contentColor = cameraScheme.onSecondaryContainer
                )
            ) {
                AppIcon(
                    iconProvider = if (isFlashOn) AlgidyIcons.Scanner.FlashOff
                    else AlgidyIcons.Scanner.FlashOn,
                )
            }
        }
    )
}