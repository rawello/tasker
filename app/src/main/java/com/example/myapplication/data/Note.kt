package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")
data class Note (
    var textNote: String,
    var description: String,
    var isDone: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}