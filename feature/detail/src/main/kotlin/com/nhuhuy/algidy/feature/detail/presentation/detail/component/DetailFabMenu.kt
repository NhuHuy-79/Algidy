package com.nhuhuy.algidy.feature.detail.presentation.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.presentation.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailFabMenu(
    onEditClick: () -> Unit,
    onConsumedClick: () -> Unit,
    onWastedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    FloatingActionButtonMenu(
        modifier = modifier,
        expanded = expanded,
        horizontalAlignment = Alignment.End,
        button = {
            MediumFloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Rounded.Close else Icons.Rounded.Edit,
                    contentDescription = stringResource(R.string.action_menu),
                )
            }
        }
    ) {
        FloatingActionButtonMenuItem(
            onClick = {
                onConsumedClick()
                expanded = false
            },
            icon = { Icon(Icons.Rounded.Restaurant, contentDescription = null) },
            text = { Text(text = stringResource(R.string.detail_fab_consume_this)) },
            containerColor = AlgidyTheme.extendedColors.consumedContainer,
            contentColor = AlgidyTheme.extendedColors.onConsumedContainer
        )

        FloatingActionButtonMenuItem(
            onClick = {
                onEditClick()
                expanded = false
            },
            icon = { Icon(Icons.Rounded.Edit, contentDescription = null) },
            text = { Text(text = stringResource(R.string.detail_fab_edit_details)) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )

        FloatingActionButtonMenuItem(
            onClick = {
                onWastedClick()
                expanded = false
            },
            icon = { Icon(Icons.Rounded.DeleteOutline, contentDescription = null) },
            text = { Text(text = stringResource(R.string.detail_fab_mark_as_wasted)) },
            containerColor = AlgidyTheme.extendedColors.wastedContainer,
            contentColor = AlgidyTheme.extendedColors.onWastedContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailFabMenuPreview() {
    AlgidyTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)) {
            DetailFabMenu(
                onEditClick = {},
                onConsumedClick = {},
                onWastedClick = {},
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
