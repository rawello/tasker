package com.example.myapplication.domain.usecase

import com.example.myapplication.data.model.toDataModel
import com.example.myapplication.data.repository.NoteRepository
import com.example.myapplication.domain.model.Note

class UpdateNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        repository.updateNote(note.toDataModel())
    }
}