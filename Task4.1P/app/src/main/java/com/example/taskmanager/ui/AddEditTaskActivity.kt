package com.example.taskmanager.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.databinding.ActivityAddEditTaskBinding
import com.example.taskmanager.model.Task
import com.example.taskmanager.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditTaskBinding
    private val taskViewModel: TaskViewModel by viewModels()
    private var currentTaskId: Int? = null
    private var selectedDueDate: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add/Edit Task"

        currentTaskId = intent.getIntExtra(EXTRA_TASK_ID, -1)
            .takeIf { it != -1 }

        if (currentTaskId != null) {
            supportActionBar?.title = "Edit Task"
            taskViewModel.getTaskById(currentTaskId!!).observe(this) { task ->
                task?.let { populateTaskDetails(it) }
            }
        } else {
            supportActionBar?.title = "Add New Task"
        }

        binding.textViewSelectedDueDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.buttonSaveTask.setOnClickListener {
            saveTask()
        }
    }

    private fun populateTaskDetails(task: Task) {
        binding.editTextTaskTitle.setText(task.title)
        binding.editTextTaskDescription.setText(task.description)
        selectedDueDate = task.dueDate
        updateDueDateDisplay(task.dueDate)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        if (selectedDueDate != 0L) {
            calendar.timeInMillis = selectedDueDate
        }

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                selectedDueDate = selectedCalendar.timeInMillis
                updateDueDateDisplay(selectedDueDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun updateDueDateDisplay(timestamp: Long) {
        if (timestamp == 0L) {
            binding.textViewSelectedDueDate.text = "Select Due Date"
        } else {
            val date = Date(timestamp)
            val format = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
            binding.textViewSelectedDueDate.text = format.format(date)
        }
    }

    private fun saveTask() {
        val title = binding.editTextTaskTitle.text.toString().trim()
        val description = binding.editTextTaskDescription.text.toString().trim()


        if (TextUtils.isEmpty(title)) {
            binding.editTextTaskTitle.error = "Title is required"
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedDueDate == 0L) {
            Toast.makeText(this, "Due date is required", Toast.LENGTH_SHORT).show()
            return
        }

        val taskToSave = Task(
            id = currentTaskId ?: 0,
            title = title,
            description = description.takeIf { it.isNotEmpty() },
            dueDate = selectedDueDate
        )

        if (currentTaskId == null) {
            taskViewModel.insert(taskToSave)
        } else {
            taskViewModel.update(taskToSave)
        }

        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_TASK_ID = "com.example.taskmanager.EXTRA_TASK_ID"
    }
}
