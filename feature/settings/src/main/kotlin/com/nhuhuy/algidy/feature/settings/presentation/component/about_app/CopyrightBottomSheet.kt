package com.nhuhuy.algidy.feature.settings.presentation.component.about_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CopyrightBottomSheet(
    onDismiss: () -> Unit,
) {
    val localSpacing = LocalAlgidySpacing.current
    AppBottomSheetColumn(
        modifier = Modifier.padding(horizontal = localSpacing.large),
        onDismiss = onDismiss,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(localSpacing.small)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialShapes.Cookie12Sided.toShape()
                    ),
                contentAlignment = Alignment.Center
            ) {
                AppIcon(
                    modifier = Modifier.padding(localSpacing.medium),
                    iconProvider = AlgidyIcons.Settings.License,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Text(
                text = stringResource(R.string.setting_license),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
        ) {
            item {
                Text(
                    text = stringResource(com.nhuhuy.algidy.feature.settings.R.string.apache_license_2_0_content),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}