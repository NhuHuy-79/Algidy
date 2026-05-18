package com.nhuhuy.algidy.core.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun PhotoPickerContainer(
    onImagePicked: (Uri?) -> Unit,
    content: @Composable (onPhotoPick: () -> Unit) -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onImagePicked(uri) }
    )
    val launchPicker = {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
    content(launchPicker)
}


@Composable
fun PlaceholderImage(
    onImagePicked: (Uri) -> Unit
){
    PhotoPickerContainer(
        onImagePicked = { uri ->
            uri?.let { onImagePicked(it) }
        }
    ) {

    }
}