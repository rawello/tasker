package com.example.myapplication.presentation.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    onTimeSelected: (Calendar) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedTime by rememberSaveable { mutableStateOf(Calendar.getInstance()) }

    val timePickerState = androidx.compose.material3.rememberTimePickerState(
        initialHour = selectedTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = selectedTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_time_title)) },
        text = {
            androidx.compose.material3.TimePicker(
                state = timePickerState
            )
        },
        confirmButton = {
            Button(onClick = {
                selectedTime.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                selectedTime.set(Calendar.MINUTE, timePickerState.minute)
                onTimeSelected(selectedTime)
            }) {
                Text(stringResource(R.string.confirm_button))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    )
}