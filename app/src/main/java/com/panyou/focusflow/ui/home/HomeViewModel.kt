package com.panyou.focusflow.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panyou.focusflow.data.local.entity.Task
import com.panyou.focusflow.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _currentListId = MutableStateFlow("default-inbox-id")

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    val tasks: StateFlow<List<Task>> = _currentListId
        .flatMapLatest { listId ->
            taskRepository.getTasksForList(listId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(title: String) {
        viewModelScope.launch {
            val newTask = Task(
                id = UUID.randomUUID().toString(),
                listId = _currentListId.value,
                title = title,
                createdAt = System.currentTimeMillis()
            )
            taskRepository.insertTask(newTask)
        }
    }

    fun onTaskCheckedChange(task: Task, isChecked: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = isChecked))
        }
    }
    
    fun onTaskDelete(task: Task) {
        viewModelScope.launch {
            taskRepository.softDeleteTask(task.id)
        }
    }
}
