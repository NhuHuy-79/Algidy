package com.nhuhuy.algidy.feature.food_entry.presentation.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.PhotoPickerContainer
import com.nhuhuy.algidy.core.presentation.component.FoodImage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ImageAndNameField(
    modifier: Modifier = Modifier,
    imageUri: String? = null,
    name: String = "",
    onImageUriChange: (uri: Uri) -> Unit,
    onEditClick: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    ListItem(
        shapes = ListItemDefaults.shapes(shape = RoundedCornerShape(48.dp)),
        modifier = modifier,
        colors = ListItemDefaults.colors(
            containerColor = scheme.primaryContainer,
            headlineColor = scheme.onPrimaryContainer,
            overlineColor = scheme.onPrimaryContainer,
        ),
        onClick = onEditClick,
        leadingContent = {
            PhotoPickerContainer(
                onImagePicked = { uri ->
                    uri?.let { onImageUriChange(uri) }
                }
            ) { launcher ->
                ImagePickerIcon(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = scheme.primary,
                            shape = MaterialShapes.Sunny.toShape()
                        ),
                    imageUri = imageUri,
                    onAddImageClick = launcher,
                )
            }
        },
        verticalAlignment = Alignment.CenterVertically,
        trailingContent = {
            FilledTonalIconButton(
                onClick = onEditClick,
                shape = CircleShape,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = scheme.primary,
                    contentColor = scheme.onPrimary
                )
            ) {
                AppIcon(
                    iconProvider = AlgidyIcons.FoodEntry.EditFood,
                )
            }
        },
        overlineContent = {
            Text(
                text = "Food Name",
                fontWeight = FontWeight.SemiBold
            )
        }
    ) {
        Text(
            text = name.ifBlank { "Click Edit to add your food name" },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ImagePickerIcon(
    imageUri: String?,
    onAddImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val localSpacing = LocalAlgidySpacing.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (imageUri.isNullOrBlank()) {
            ImagePickerIcon(
                modifier = Modifier.padding(localSpacing.large),
                onClick = onAddImageClick
            )
        } else {
            FoodImage(
                modifier = Modifier.padding(localSpacing.large),
                imageUrl = imageUri
            )
        }
    }
}

@Composable
private fun ImagePickerIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        AppIcon(iconProvider = AlgidyIcons.FoodEntry.PickImage)
    }
}

@Preview
@Composable
fun NameAndImageFieldPreview() {
    AlgidyTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            ImageAndNameField(
                modifier = Modifier.fillMaxSize(),
                imageUri = null,
                name = "",
                onImageUriChange = {},
                onEditClick = {}
            )
        }
    }
}