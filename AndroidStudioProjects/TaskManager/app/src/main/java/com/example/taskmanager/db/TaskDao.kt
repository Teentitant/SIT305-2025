package com.example.taskmanager.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskmanager.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)


    @Update
    suspend fun updateTask(task: Task)


    @Delete
    suspend fun deleteTask(task: Task)


    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasksSortedByDueDate(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>
}