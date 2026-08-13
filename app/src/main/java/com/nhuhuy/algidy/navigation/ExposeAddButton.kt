package com.nhuhuy.algidy.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R

private enum class AddFabItem(
    val icon: ImageVector,
    @field:StringRes val label: Int,
) {
    CAMERA(
        icon = Icons.Default.CameraAlt,
        label = R.string.inventory_scanner_btn
    ),
    MANUAL(
        icon = Icons.Default.Edit,
        label = R.string.inventory_manually_btn
    )
}

@Composable
fun ExposeAddButton(
    modifier: Modifier = Modifier,
    onCameraClick: () -> Unit,
    onManualClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val localShape = LocalAlgidyShapes.current
    val localSpacing = LocalAlgidySpacing.current
    Box(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier
                .height(56.dp)
                .animateContentSize(alignment = Alignment.CenterEnd),
            onClick = { expanded = !expanded },
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null
            )

            AnimatedVisibility(
                visible = !expanded,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End)
            ) {
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.inventory_manually_btn),
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(localSpacing.extraSmall))

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier,
            shape = localShape.medium
        ) {
            AddFabItem.entries.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        when (item) {
                            AddFabItem.CAMERA -> {
                                onCameraClick()
                            }

                            AddFabItem.MANUAL -> {
                                onManualClick()
                            }
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(item.label),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}