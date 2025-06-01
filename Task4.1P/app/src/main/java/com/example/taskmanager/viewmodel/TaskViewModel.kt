package com.example.taskmanager.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.taskmanager.db.AppDatabase
import com.example.taskmanager.db.TaskDao
import com.example.taskmanager.model.Task
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskDao: TaskDao
    val allTasks: LiveData<List<Task>>

    init {
        taskDao = AppDatabase.getDatabase(application).taskDao()
        allTasks = taskDao.getAllTasksSortedByDueDate() // Fetches tasks sorted by due date [cite: 6]
    }

    fun insert(task: Task) = viewModelScope.launch {
        taskDao.insertTask(task) // Create operation [cite: 5]
    }

    fun update(task: Task) = viewModelScope.launch {
        taskDao.updateTask(task) // Update operation [cite: 5, 7]
    }

    fun delete(task: Task) = viewModelScope.launch {
        taskDao.deleteTask(task) // Delete operation [cite: 5, 8]
    }

    fun getTaskById(taskId: Int): LiveData<Task> {
        return taskDao.getTaskById(taskId)
    }
}