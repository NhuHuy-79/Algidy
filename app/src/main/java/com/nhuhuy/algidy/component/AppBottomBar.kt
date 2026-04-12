package com.nhuhuy.algidy.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Camera
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.navigation.Route
import com.nhuhuy.algidy.navigation.toBottomBarIcon

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppBottomBar(
    currentRoute: Route,
    bottomBarIcons: List<Route>,
    onRouteSelected: (Route) -> Unit,
    onScannerPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalFloatingToolbar(
        modifier = modifier,
        expanded = true,
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onScannerPress,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Camera,
                    contentDescription = "Scan",
                )
            }
        },
    ) {
        bottomBarIcons.forEach { route ->
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
                        imageVector = route.toBottomBarIcon(),
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppBottomBarPreview() {

}