package com.nhuhuy.algidy.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppBottomSheet
import com.nhuhuy.algidy.core.designsystem.component.AppButton
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Algidy ${versionFeatures.versionName}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
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
                    items(
                        count = features.size,
                    ) { index ->
                        val feature = features[index]
                        val shape = index.toItemPosition(features.size).toVerticalSegmentedShape()
                        ListItem(
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape),
                            headlineContent = {
                                Text(
                                    text = feature.title,
                                    fontWeight = FontWeight.Black
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = feature.description
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
                    items(
                        count = fixItems.size
                    ) { index ->
                        val fixItem = fixItems[index]
                        val shape = index.toItemPosition(features.size).toVerticalSegmentedShape()
                        ListItem(
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
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
