package com.nhuhuy.algidy.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Inventory
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppBottomBar(
    currentRoute: ImageVector,
    onRouteSelected: (ImageVector) -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalFloatingToolbar(
        modifier = modifier,
        expanded = true,
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Mở màn hình Scan */ },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Camera,
                    contentDescription = "Scan",
                )
            }
        },
    ) {
        routes.forEach { route ->
            val isSelected = currentRoute == route

            IconButton(
                onClick = { onRouteSelected(route) },
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else Color.Transparent
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = route,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

val routes = listOf(
    Icons.Rounded.Home,
    Icons.Rounded.AcUnit,
    Icons.Rounded.Inventory,
    Icons.Rounded.Analytics,
    Icons.Rounded.Settings
)


@Preview
@Composable
private fun AppBottomBarPreview() {
    AlgidyTheme {
        AppBottomBar(
            currentRoute = Icons.Rounded.Home,
            onRouteSelected = {}
        )
    }
}