package com.nhuhuy.algidy.core.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun PhotoPickerContainer(
    onImagePicked: (Uri?) -> Unit,
    onLaunch: () -> Unit = {},
    content: @Composable (() -> Unit) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onImagePicked(uri) }
    )

    content {
        onLaunch()
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}
