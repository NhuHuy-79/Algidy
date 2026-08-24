package com.nhuhuy.algidy.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppBottomSheet
import com.nhuhuy.algidy.core.designsystem.component.AppButton
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.model.AppNewFeature
import com.nhuhuy.algidy.core.model.FixItem
import com.nhuhuy.algidy.core.model.VersionFeatures
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppNewFeatureBottomSheet(
    versionFeatures: VersionFeatures,
    onDismiss: () -> Unit,
) {
    AppBottomSheet(
        onDismiss = onDismiss
    ) {
        val localSpacing = LocalAlgidySpacing.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            shape = MaterialShapes.Pill.toShape()
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AppIcon(
                        modifier = Modifier.padding(localSpacing.medium),
                        iconProvider = AlgidyIcons.Settings.NewFeature,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = "Algidy ${versionFeatures.versionName}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    if (versionFeatures.features.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "New Features",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Black,
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                versionFeatures.apply {
                    itemsIndexed(
                        items = features,
                    ) { index: Int, appNewFeature: AppNewFeature ->
                        val shape = index.toItemPosition(features.size).toVerticalSegmentedShape()
                        ListItem(
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape),
                            headlineContent = {
                                Text(
                                    text = appNewFeature.title,
                                    fontWeight = FontWeight.Black
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = appNewFeature.description
                                )
                            }
                        )
                    }
                }

                item {
                    if (versionFeatures.fixItems.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Fix Bugs",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Black,
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                versionFeatures.apply {
                    itemsIndexed(
                        items = fixItems,
                    ) { index: Int, fixItem: FixItem ->
                        val shape = index.toItemPosition(fixItems.size).toVerticalSegmentedShape()
                        ListItem(
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape),
                            headlineContent = {
                                Text(
                                    text = fixItem.title,
                                    fontWeight = FontWeight.Black
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = fixItem.description
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(localSpacing.large))

            AppButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                text = "Continue",
                icon = Icons.Rounded.TaskAlt,
                onClick = onDismiss
            )
        }
    }
}
