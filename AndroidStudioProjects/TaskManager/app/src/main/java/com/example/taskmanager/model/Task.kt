package com.example.taskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks") // Marks this class as a database table [cite: 9]
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var description: String?,
    var dueDate: Long // Store as Long (timestamp) for easier sorting
)