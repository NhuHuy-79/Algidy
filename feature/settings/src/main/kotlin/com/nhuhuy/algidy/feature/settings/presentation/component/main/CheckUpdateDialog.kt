package com.nhuhuy.algidy.feature.settings.presentation.component.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.UiResult
import com.nhuhuy.algidy.core.presentation.UiResultContainer

@Composable
fun CheckUpdateDialog(
    uiState: UiResult<String?>,
    currentVersion: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.setting_check_update_title),
                textAlign = TextAlign.Center
            )
        },
        icon = {
            AppIcon(iconProvider = AlgidyIcons.Settings.CheckUpdate)
        },
        text = {
            UiResultContainer(
                state = uiState,
                loading = {
                    CheckUpdateLoading()
                },
                success = { version ->
                    if (version != null) {
                        CheckUpdateSuccessResult(
                            newVersion = version,
                            currentVersion = currentVersion,
                        )
                    } else {
                        CheckUpdateErrorResult()
                    }
                },
                error = {
                    CheckUpdateErrorResult()
                },
                idle = {},
            )
        },
        confirmButton = {
            when (uiState) {
                is UiResult.Success -> {
                    if (uiState.data != null &&
                        uiState.data != currentVersion
                    ) {
                        FilledTonalButton(
                            onClick = onConfirm
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.check_update_dialog_success_updated
                                )
                            )
                        }
                    } else {
                        TextButton(
                            onClick = onDismiss
                        ) {
                            Text(
                                text = stringResource(R.string.action_cancel)
                            )
                        }
                    }
                }

                is UiResult.Failure -> {
                    FilledTonalButton(
                        onClick = onConfirm
                    ) {
                        Text(
                            text = stringResource(R.string.check_update_dialog_error)
                        )
                    }
                }

                else -> Unit
            }
        },
        dismissButton = {
            when (uiState) {
                is UiResult.Failure -> {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text(
                            text = stringResource(R.string.action_cancel)
                        )
                    }
                }

                else -> Unit
            }
        },
    )
}

@Composable
private fun CheckUpdateSuccessResult(
    newVersion: String,
    currentVersion: String,
    modifier: Modifier = Modifier,
) {
    val isUpdateAvailable = newVersion != currentVersion

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = stringResource(
                if (isUpdateAvailable) {
                    R.string.check_update_dialog_update_available
                } else {
                    R.string.check_update_dialog_update_confirm_btn
                }
            ),
            style = MaterialTheme.typography.titleMedium,
        )

        if (isUpdateAvailable) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = currentVersion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = "→",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = newVersion,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        } else {
            Text(
                text = currentVersion,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun CheckUpdateErrorResult(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.CloudOff,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.error,
        )

        Text(
            text = stringResource(
                R.string.check_update_dialog_error
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun CheckUpdateLoading(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ContainedLoadingIndicator()

        Text(
            text = stringResource(R.string.check_update_dialog_loading),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}