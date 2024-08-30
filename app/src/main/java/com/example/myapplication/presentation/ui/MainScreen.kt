package com.example.myapplication.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.domain.model.Note
import com.example.myapplication.presentation.ui.components.AddNote
import com.example.myapplication.presentation.ui.components.NoteItem
import com.example.myapplication.presentation.ui.components.TopBar
import com.example.myapplication.presentation.viewmodel.NoteViewModel

@Composable
fun MainScreen(viewModel: NoteViewModel) {
    val isVisible = rememberSaveable { mutableStateOf(true) }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var selectedNote by rememberSaveable { mutableStateOf<Note?>(null) }
    val notes by viewModel.getAllNotes().collectAsState(initial = emptyList())

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -1) {
                    isVisible.value = false
                }

                if (available.y > 1) {
                    isVisible.value = true
                }

                return Offset.Zero
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(stringResource(R.string.top_bar_title))
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AnimatedVisibility(
                visible = isVisible.value,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        showBottomSheet = true
                        selectedNote = null
                    }
                ) {
                    Icon(Icons.Filled.Add, stringResource(R.string.add_button_label))
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (notes.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_notes_message),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(nestedScrollConnection),
                    contentPadding = innerPadding
                ) {
                    items(notes) { note ->
                        NoteItem(
                            note = note,
                            onNoteClick = { clickedNote ->
                                showBottomSheet = true
                                selectedNote = clickedNote
                            },
                            onSwipeLeft = { swipedNote ->
                                swipedNote.isDone = !swipedNote.isDone
                                viewModel.updateNote(swipedNote)
                            },
                            onSwipeRight = { swipedNote ->
                                viewModel.deleteNote(swipedNote)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        AddNote(
            note = selectedNote,
            onDismiss = {
                showBottomSheet = false
                selectedNote = null
            },
            onUpdateNote = { updatedNote ->
                viewModel.updateNote(updatedNote)
            },
            onDeleteNote = { deletedNote ->
                viewModel.deleteNote(deletedNote)
            },
            onAddNote = { newNote ->
                viewModel.insertNote(newNote)
            }
        )
    }
}