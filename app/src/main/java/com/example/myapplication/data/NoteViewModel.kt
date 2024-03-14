package com.example.myapplication.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application.applicationContext)

    fun getAllNotes(): Flow<List<Note>> {
        return db.noteDao().getNotes()
    }

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            db.noteDao().insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            db.noteDao().updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            db.noteDao().deleteNote(note)
        }
    }
}