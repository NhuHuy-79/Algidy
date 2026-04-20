package com.nhuhuy.algidy.feature.scanner

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.feature.scanner.presentation.ScannerScreen

@Composable
fun ScannerRoute(
    onNavigateBack: () -> Unit
) {
    BoxLayout {
        ScannerScreen(
            onClosePress = onNavigateBack
        )
    }
}