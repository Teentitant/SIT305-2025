package com.example.taskmanager.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskmanager.model.Task

@Dao
interface TaskDao {
    // Create operation [cite: 5]
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    // Update operation [cite: 5, 7]
    @Update
    suspend fun updateTask(task: Task)

    // Delete operation [cite: 5, 8]
    @Delete
    suspend fun deleteTask(task: Task)

    // Read operation: Get all tasks, sorted by due date [cite: 5, 6]
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasksSortedByDueDate(): LiveData<List<Task>>

    // Read operation: Get a single task by its ID
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>
}