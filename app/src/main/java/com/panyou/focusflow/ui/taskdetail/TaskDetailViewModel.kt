package com.panyou.focusflow.ui.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.panyou.focusflow.BuildConfig
import com.panyou.focusflow.data.local.entity.Subtask
import com.panyou.focusflow.data.local.entity.Task
import com.panyou.focusflow.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    // --- Gemini AI Setup ---
    private val generativeModel by lazy {
        try {
            GenerativeModel(
                modelName = "gemini-pro",
                apiKey = BuildConfig.geminiApiKey
            )
        } catch (e: Exception) {
            null
        }
    }

    private val _taskId = MutableStateFlow<String?>(null)

    val task: StateFlow<Task?> = _taskId.flatMapLatest { id ->
        if (id == null) flowOf(null) else flowOf(taskRepository.getTaskById(id)) 
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

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
        val model = generativeModel
        if (model == null) {
            android.util.Log.e("FocusFlow", "Gemini Model not initialized - check API Key")
            return 
        }
        _uiState.value = _uiState.value.copy(isGenerating = true)
        
        viewModelScope.launch {
            try {
                val prompt = "Break down this task into 3-5 actionable subtasks (short titles only, no numbering): ${currentTask.title}"
                val response = model.generateContent(prompt)
                
                val rawText = response.text ?: ""
                val lines = rawText.lines()
                    .filter { it.isNotBlank() }
                    .map { it.replace(Regex("^[-*\\d.]+\\s+"), "").trim() } // Remove bullets/numbers

                val aiSubtasks = lines.mapIndexed { index, title ->
                    Subtask(
                        taskId = currentTask.id,
                        title = title,
                        sortOrder = _uiState.value.subtasks.size + index
                    )
                }
                
                taskRepository.insertSubtasks(aiSubtasks)
            } catch (e: Exception) {
                // TODO: Handle AI error (e.g., show snackbar)
                e.printStackTrace()
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
