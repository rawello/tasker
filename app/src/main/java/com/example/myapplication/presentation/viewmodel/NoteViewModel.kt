package com.example.myapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.repository.NoteRepository
import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.usecase.AddNoteUseCase
import com.example.myapplication.domain.usecase.DeleteNoteUseCase
import com.example.myapplication.domain.usecase.GetNotesUseCase
import com.example.myapplication.domain.usecase.SetReminderUseCase
import com.example.myapplication.domain.usecase.UpdateNoteUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    private val setReminderUseCase: SetReminderUseCase

    init {
        val noteDao = AppDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        setReminderUseCase = SetReminderUseCase(application)
    }

    private val getNotesUseCase = GetNotesUseCase(repository)
    private val addNoteUseCase = AddNoteUseCase(repository)
    private val updateNoteUseCase = UpdateNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)

    fun getAllNotes(): Flow<List<Note>> = getNotesUseCase()

    fun insertNote(note: Note) {
        viewModelScope.launch {
            addNoteUseCase(note)
            setReminderUseCase(note, note.reminderTime)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            updateNoteUseCase(note)
            setReminderUseCase(note, note.reminderTime)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase(note)
            setReminderUseCase(note, note.reminderTime)
        }
    }
}