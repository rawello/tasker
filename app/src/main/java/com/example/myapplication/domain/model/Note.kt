package com.example.myapplication.domain.model

data class Note(
    val id: Int,
    var textNote: String,
    var description: String,
    var isDone: Boolean,
    var reminderTime: Long
)