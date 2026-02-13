package com.panyou.focusflow.data.repository

import com.panyou.focusflow.data.local.dao.TaskDao
import com.panyou.focusflow.data.local.entity.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    // Get all tasks for a specific list (Default to "Inbox" or main list for now)
    // For V1, we can assume a default "My Day" or "Tasks" list ID if none provided
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
}
