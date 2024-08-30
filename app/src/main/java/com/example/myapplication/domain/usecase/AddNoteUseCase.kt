package com.example.myapplication.domain.usecase

import com.example.myapplication.data.model.toDataModel
import com.example.myapplication.data.repository.NoteRepository
import com.example.myapplication.domain.model.Note

class AddNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        repository.insertNote(note.toDataModel())
    }
}