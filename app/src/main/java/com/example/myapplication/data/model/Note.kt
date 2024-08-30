package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.domain.model.Note as DomainNote

@Entity(tableName = "Notes")
data class Note(
    var textNote: String,
    var description: String,
    var isDone: Boolean,
    var reminderTime: Long = 0L
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun Note.toDomainModel(): DomainNote {
    return DomainNote(
        id = this.id,
        textNote = this.textNote,
        description = this.description,
        isDone = this.isDone,
        reminderTime = this.reminderTime
    )
}

fun DomainNote.toDataModel(): Note {
    return Note(
        textNote = this.textNote,
        description = this.description,
        isDone = this.isDone,
        reminderTime = this.reminderTime
    ).apply { id = this@toDataModel.id }
}