package com.nhuhuy.algidy.core.presentation

import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

@Composable
fun PhotoPickerContainer(
    onImagePicked: (Uri?) -> Unit,
    content: @Composable (onPhotoPick: () -> Unit) -> Unit,
) {
    val owner = LocalActivityResultRegistryOwner.current

    if (owner != null) {
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
    } else {
        val context = LocalContext.current
        val activity = context.findActivity()
        if (activity != null) {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides activity) {
                PhotoPickerContainer(
                    onImagePicked = onImagePicked,
                    content = content
                )
            }
        } else {
            // Fallback for previews or non-activity contexts
            content { }
        }
    }
}

private tailrec fun Context.findActivity(): ActivityResultRegistryOwner? {
    return when (this) {
        is ActivityResultRegistryOwner -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}

@Composable
fun PlaceholderImage(
    onImagePicked: (Uri) -> Unit,
) {
    PhotoPickerContainer(
        onImagePicked = { uri ->
            uri?.let { onImagePicked(it) }
        }
    ) { _ ->
        // This is just a placeholder, in a real scenario you'd call onPhotoPick() on click
    }
}