package com.example.myapplication.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.domain.model.Note
import java.util.Calendar

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(
    note: Note?,
    onDismiss: () -> Unit,
    onUpdateNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onAddNote: (Note) -> Unit
) {
    var title by rememberSaveable { mutableStateOf(note?.textNote ?: "") }
    var description by rememberSaveable { mutableStateOf(note?.description ?: "") }
    var isDone by rememberSaveable { mutableStateOf(note?.isDone ?: false) }
    var reminderTime by rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var showSnackbar by rememberSaveable { mutableStateOf(false) }
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }
    val pattern = Regex("\\s+")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.title_label)) }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description_label)) }
                )
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.done_label),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Switch(
                        checked = isDone,
                        onCheckedChange = { isDone = it },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Button(
                    onClick = { showAlertDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (reminderTime == System.currentTimeMillis()) stringResource(R.string.set_reminder_button) else stringResource(R.string.change_reminder_button))
                }

                if (showAlertDialog) {
                    AlertDialog(
                        onDismissRequest = { showAlertDialog = false },
                        title = { Text(stringResource(R.string.change_reminder_button)) },
                        text = { Text(stringResource(R.string.change_reminder_text)) },
                        confirmButton = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        showDatePicker = true
                                        showAlertDialog = false
                                    }
                                ) {
                                    Text(stringResource(R.string.change_date_button))
                                }
                                Button(
                                    onClick = {
                                        showTimePicker = true
                                        showAlertDialog = false
                                    }
                                ) {
                                    Text(stringResource(R.string.change_time_button))
                                }
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showAlertDialog = false },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(stringResource(R.string.close_button))
                            }
                        }
                    )
                }

                if (showDatePicker) {
                    DatePicker(
                        onDateSelected = { date ->
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = reminderTime
                            calendar.set(Calendar.YEAR, date.get(Calendar.YEAR))
                            calendar.set(Calendar.MONTH, date.get(Calendar.MONTH))
                            calendar.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH))
                            reminderTime = calendar.timeInMillis
                            showDatePicker = false
                        },
                        onDismiss = { showDatePicker = false }
                    )
                }

                if (showTimePicker) {
                    TimePicker(
                        onTimeSelected = { time ->
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = reminderTime
                            calendar.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY))
                            calendar.set(Calendar.MINUTE, time.get(Calendar.MINUTE))
                            reminderTime = calendar.timeInMillis
                            showTimePicker = false
                        },
                        onDismiss = { showTimePicker = false }
                    )
                }

                if (note != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (title.isBlank()) {
                                    showSnackbar = true
                                } else {
                                    note.textNote = title.replace(pattern, " ")
                                    note.description = description.replace(pattern, " ")
                                    note.isDone = isDone
                                    note.reminderTime = reminderTime
                                    onUpdateNote(note)
                                    onDismiss()
                                }
                            },
                            enabled = true,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(stringResource(R.string.edit_button))
                        }
                        Button(
                            onClick = {
                                onDeleteNote(note)
                                onDismiss()
                            },
                            enabled = true,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(stringResource(R.string.delete_button))
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                showSnackbar = true
                            } else {
                                val newNote = Note(
                                    id = 0,
                                    textNote = title.replace(pattern, " "),
                                    description = description.replace(pattern, " "),
                                    isDone = isDone,
                                    reminderTime = reminderTime)
                                onAddNote(newNote)
                                onDismiss()
                            }
                        },
                        enabled = true
                    ) {
                        Text(stringResource(R.string.add_button))
                    }
                }

                if (showSnackbar) {
                    Snackbar(
                        modifier = Modifier.padding(8.dp),
                        action = {
                            Button(onClick = { showSnackbar = false }) {
                                Text(stringResource(R.string.close_button))
                            }
                        }
                    ) {
                        Text(stringResource(id = R.string.invalid_input_message))
                    }
                }
            }
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }

    )
}