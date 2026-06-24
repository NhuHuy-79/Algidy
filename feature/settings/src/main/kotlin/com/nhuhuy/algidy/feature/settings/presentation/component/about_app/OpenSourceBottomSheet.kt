package com.nhuhuy.algidy.feature.settings.presentation.component.about_app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.nhuhuy.algidy.core.designsystem.component.AppBottomSheet

@Composable
fun OpenSourceBottomSheet(
    onDismiss: () -> Unit
) {
    AppBottomSheet(
        modifier = Modifier,
        onDismiss = onDismiss
    ) {
        LibrariesContainer()
    }

}