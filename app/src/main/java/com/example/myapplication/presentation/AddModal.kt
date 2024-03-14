package com.example.myapplication.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.Note

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddModal(
    note: Note?,
    onDismiss: () -> Unit,
    onUpdateNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onAddNote: (Note) -> Unit
) {
    var title by rememberSaveable { mutableStateOf(note?.textNote ?: "") }
    var description by rememberSaveable { mutableStateOf(note?.description ?: "") }
    var isDone by rememberSaveable { mutableStateOf(note?.isDone ?: false) }
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
                    label = { Text("Название") }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") }
                )
                Row(
                   // modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Выполнено:  ",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Switch(
                        checked = isDone,
                        onCheckedChange = { isDone = it },
                        modifier = Modifier.align(Alignment.CenterVertically)
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
                                note.textNote = title.replace(pattern, " ")
                                note.description = description.replace(pattern, " ")
                                note.isDone = isDone
                                onUpdateNote(note)
                                onDismiss()
                            },
                            enabled = true,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Изменить")
                        }
                        Button(
                            onClick = {
                                onDeleteNote(note)
                                onDismiss()
                            },
                            enabled = true,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Удалить")
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            val newNote = Note(textNote = title.replace(pattern, " "),
                                description = description.replace(pattern, " "),
                                isDone = isDone)
                            onAddNote(newNote)
                            onDismiss()
                        },
                        enabled = true
                    ) {
                        Text("Добавить")
                    }
                }
            }
        }
    )
}