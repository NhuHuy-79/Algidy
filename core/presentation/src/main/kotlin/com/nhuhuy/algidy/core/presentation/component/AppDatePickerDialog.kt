package com.nhuhuy.algidy.core.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerDisplayMode
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    initialDateMillis: Long?,
    title: String,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis ?: System.currentTimeMillis()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                onDismiss()
            }) {
                Text(stringResource(R.string.action_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp)
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog(
    hour: Int,
    is24Hour: Boolean = true,
    minutes: Int,
    title: String,
    confirmText: String,
    onDateSelected: (hour: Int, minutes: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minutes,
        is24Hour = is24Hour
    )

    var displayMode by remember { mutableStateOf(TimePickerDisplayMode.Input) }

    TimePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(timePickerState.hour, timePickerState.minute)
                    onDismiss()
                }
            ) {
                Text(
                    text = confirmText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        },
        modeToggleButton = {
            TimePickerDialogDefaults.DisplayModeToggle(
                onDisplayModeChange = {
                    displayMode = if (displayMode == TimePickerDisplayMode.Picker) {
                        TimePickerDisplayMode.Input
                    } else {
                        TimePickerDisplayMode.Picker
                    }
                },
                displayMode = displayMode
            )
        }
    ) {
        if (displayMode == TimePickerDisplayMode.Picker) {
            TimePicker(
                modifier = Modifier.padding(16.dp),
                state = timePickerState
            )
        } else {
            TimeInput(
                modifier = Modifier.padding(16.dp),
                state = timePickerState
            )
        }
    }
}
