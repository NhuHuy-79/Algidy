package com.nhuhuy.algidy.feature.settings.presentation.component.about_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppBottomSheetColumn
import com.nhuhuy.algidy.core.presentation.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PolicyBottomSheet(
    onDismiss: () -> Unit
) {
    AppBottomSheetColumn(
        modifier = Modifier.padding(8.dp),
        onDismiss = onDismiss,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = MaterialShapes.Cookie6Sided.toShape()
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Policy,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }

        Text(
            text = stringResource(R.string.setting_privacy_policy),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.privacy_policy_content),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                )
            }
        }
    }
}