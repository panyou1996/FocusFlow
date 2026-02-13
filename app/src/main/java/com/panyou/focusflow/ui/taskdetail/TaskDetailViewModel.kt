package com.panyou.focusflow.ui.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panyou.focusflow.data.local.entity.Subtask
import com.panyou.focusflow.data.local.entity.Task
import com.panyou.focusflow.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _taskId = MutableStateFlow<String?>(null)

    val task: StateFlow<Task?> = _taskId.flatMapLatest { id ->
        if (id == null) flowOf(null) else flowOf(taskRepository.getTaskById(id)) // This is one-shot, ideally should observe
        // For editing, we might want a MutableStateFlow that initializes from DB
        // But for MVVM + Room flow, let's keep it simple:
        // We actually need a Flow<Task?> from repo. Let's add that to Repo later.
        // For now, let's just load it once into a mutable state for editing.
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Better approach for Detail: Load into mutable state for editing
    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState

    fun loadTask(taskId: String) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId)
            if (task != null) {
                // Subscribe to subtasks
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

    // AI Mock Logic
    fun generateSubtasksWithAI() {
        val currentTask = _uiState.value.task ?: return
        _uiState.value = _uiState.value.copy(isGenerating = true)
        
        viewModelScope.launch {
            // TODO: Replace with real Gemini API call
            // Simulate network delay
            kotlinx.coroutines.delay(1500)
            
            val aiSubtasks = listOf(
                Subtask(taskId = currentTask.id, title = "Research ${currentTask.title}", sortOrder = 0),
                Subtask(taskId = currentTask.id, title = "Draft outline", sortOrder = 1),
                Subtask(taskId = currentTask.id, title = "Review and refine", sortOrder = 2)
            )
            taskRepository.insertSubtasks(aiSubtasks)
            _uiState.value = _uiState.value.copy(isGenerating = false)
        }
    }
}

data class TaskDetailUiState(
    val task: Task? = null,
    val subtasks: List<Subtask> = emptyList(),
    val isLoading: Boolean = true,
    val isGenerating: Boolean = false
)
