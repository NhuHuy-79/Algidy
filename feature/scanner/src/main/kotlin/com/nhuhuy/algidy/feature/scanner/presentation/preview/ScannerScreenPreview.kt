package com.nhuhuy.algidy.feature.scanner.presentation.preview

import androidx.compose.runtime.Composable
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.designsystem.theme.surfaceDark
import com.nhuhuy.algidy.core.presentation.preview.MarketPreview
import com.nhuhuy.algidy.core.presentation.preview.StoreMarketingScreen
import com.nhuhuy.algidy.feature.scanner.R
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerScreen
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerUiState

@Composable
@MarketPreview
fun ScannerScreenPreview() {
    AlgidyTheme {
        StoreMarketingScreen(
            headlineLines = listOf(
                "Scan your",
                "barcode"
            ),
            bottomBarVisible = false,
            screenColor = surfaceDark
        ) {
            ScannerScreen(
                uiState = ScannerUiState(
                    isAutoScanned = false
                ),
                onAction = {},
                onClosePress = {},
                previewPlaceholder = R.drawable.barocde
            )
        }
    }
}