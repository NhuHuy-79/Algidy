package com.nhuhuy.algidy.feature.scanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.feature.scanner.presentation.ScannerScreen
import com.nhuhuy.algidy.feature.scanner.presentation.component.ScannerMode
import com.nhuhuy.algidy.feature.scanner.viewmodel.ScannerAction
import com.nhuhuy.algidy.feature.scanner.viewmodel.ScannerViewModel
import java.util.Scanner

@Composable
fun ScannerRoute(
    viewModel: ScannerViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    BoxLayout {
        ScannerScreen(
            uiState = uiState,
            onClosePress = onNavigateBack,
            onFlashPress = { isFlashOn: Boolean ->
                onAction(ScannerAction.OnFlashChange(isFlashOn))
            },
            onAutoScanPress = { autoScanned: Boolean ->
                onAction(ScannerAction.OnAutoScanChange(autoScanned))
            },
            onScannerModePress = { mode: ScannerMode ->
                onAction(ScannerAction.OnScannerModeChange(mode))
            },
            onResultDetected = { barcodeString: String ->
                onAction(ScannerAction.OnResultDetected(barcodeString))
            }
        )
    }
}