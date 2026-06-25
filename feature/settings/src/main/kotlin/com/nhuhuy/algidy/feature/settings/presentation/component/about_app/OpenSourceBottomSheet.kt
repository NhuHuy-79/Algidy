package com.nhuhuy.algidy.feature.settings.presentation.component.about_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BuildCircle
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.nhuhuy.algidy.core.designsystem.component.AppBottomSheetColumn
import com.nhuhuy.algidy.core.presentation.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OpenSourceBottomSheet(
    onDismiss: () -> Unit
) {
    AppBottomSheetColumn(
        modifier = Modifier
            .padding(16.dp),
        onDismiss = onDismiss
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
                imageVector = Icons.Rounded.BuildCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }

        Text(
            text = stringResource(R.string.setting_open_source),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        LibrariesContainer(
            modifier = Modifier.clip(RoundedCornerShape(8.dp))
        )
    }

}