package com.example.myapplication.domain.usecase

import com.example.myapplication.data.model.toDomainModel
import com.example.myapplication.data.repository.NoteRepository
import com.example.myapplication.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes().map { notes ->
            notes.map { it.toDomainModel() }
        }
    }
}