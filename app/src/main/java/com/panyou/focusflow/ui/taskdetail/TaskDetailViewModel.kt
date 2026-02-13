package com.panyou.focusflow.ui.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// import com.google.ai.client.generativeai.GenerativeModel
import com.panyou.focusflow.data.local.entity.Subtask
import com.panyou.focusflow.data.local.entity.Task
import com.panyou.focusflow.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState

    fun loadTask(taskId: String) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId)
            if (task != null) {
                taskRepository.getSubtasksForTask(taskId).collect { subtasks ->
                    _uiState.value = _uiState.value.copy(
                        task = task,
                        subtasks = subtasks,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateTitle(newTitle: String) {
        val currentTask = _uiState.value.task ?: return
        val updatedTask = currentTask.copy(title = newTitle)
        _uiState.value = _uiState.value.copy(task = updatedTask)
        viewModelScope.launch { taskRepository.updateTask(updatedTask) }
    }

    fun updateNote(newNote: String) {
        val currentTask = _uiState.value.task ?: return
        val updatedTask = currentTask.copy(noteContent = newNote)
        _uiState.value = _uiState.value.copy(task = updatedTask)
        viewModelScope.launch { taskRepository.updateTask(updatedTask) }
    }

    fun addSubtask(title: String) {
        val currentTask = _uiState.value.task ?: return
        val newSubtask = Subtask(
            taskId = currentTask.id,
            title = title,
            sortOrder = _uiState.value.subtasks.size
        )
        viewModelScope.launch {
            taskRepository.insertSubtasks(listOf(newSubtask))
        }
    }

    fun toggleSubtask(subtask: Subtask, isChecked: Boolean) {
        viewModelScope.launch {
            taskRepository.toggleSubtaskCompletion(subtask, isChecked)
        }
    }

    fun generateSubtasksWithAI() {
        val currentTask = _uiState.value.task ?: return
        _uiState.value = _uiState.value.copy(isGenerating = true)
        
        viewModelScope.launch {
            try {
                kotlinx.coroutines.delay(1000)
                val aiSubtasks = listOf(
                    Subtask(taskId = currentTask.id, title = "Step 1 (Mock)", sortOrder = 0)
                )
                taskRepository.insertSubtasks(aiSubtasks)
            } finally {
                _uiState.value = _uiState.value.copy(isGenerating = false)
            }
        }
    }
}

data class TaskDetailUiState(
    val task: Task? = null,
    val subtasks: List<Subtask> = emptyList(),
    val isLoading: Boolean = true,
    val isGenerating: Boolean = false
)
