package com.panyou.focusflow.data.repository

import com.panyou.focusflow.data.local.dao.SubtaskDao
import com.panyou.focusflow.data.local.dao.TaskDao
import com.panyou.focusflow.data.local.dao.TaskListDao
import com.panyou.focusflow.data.local.entity.Subtask
import com.panyou.focusflow.data.local.entity.Task
import com.panyou.focusflow.data.local.entity.TaskList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val subtaskDao: SubtaskDao,
    private val taskListDao: TaskListDao // Added
) {
    // --- List methods ---
    fun getAllLists(): Flow<List<TaskList>> = taskListDao.getAllLists()
    
    suspend fun insertList(list: TaskList) = taskListDao.insertList(list)
    
    suspend fun ensureDefaultListExists() {
        val defaultId = "default-inbox-id"
        val existing = taskListDao.getListById(defaultId)
        if (existing == null) {
            taskListDao.insertList(
                TaskList(
                    id = defaultId,
                    title = "Inbox",
                    sortOrder = 0
                )
            )
        }
    }

    // --- Task methods ---
    fun getTasksForList(listId: String): Flow<List<Task>> = taskDao.getTasksByList(listId)

    fun getImportantTasks(): Flow<List<Task>> = taskDao.getImportantTasks()

    suspend fun getTaskById(taskId: String): Task? = taskDao.getTaskById(taskId)

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun softDeleteTask(taskId: String) = taskDao.softDeleteTask(taskId)

    suspend fun toggleTaskCompletion(taskId: String, isCompleted: Boolean) {
        val task = getTaskById(taskId)
        task?.let {
            updateTask(it.copy(isCompleted = isCompleted))
        }
    }

    // --- Subtask methods ---
    fun getSubtasksForTask(taskId: String): Flow<List<Subtask>> = subtaskDao.getSubtasksForTask(taskId)

    suspend fun insertSubtasks(subtasks: List<Subtask>) = subtaskDao.insertSubtasks(subtasks)

    suspend fun updateSubtask(subtask: Subtask) = subtaskDao.updateSubtask(subtask)
    
    suspend fun toggleSubtaskCompletion(subtask: Subtask, isCompleted: Boolean) {
        updateSubtask(subtask.copy(isCompleted = isCompleted))
    }
}
