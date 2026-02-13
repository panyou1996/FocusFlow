package com.panyou.focusflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.panyou.focusflow.data.local.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE listId = :listId AND isDeleted = 0 ORDER BY isCompleted ASC, sortOrder ASC, createdAt DESC")
    fun getTasksByList(listId: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("UPDATE tasks SET isDeleted = 1 WHERE id = :taskId")
    suspend fun softDeleteTask(taskId: String)

    @Query("SELECT * FROM tasks WHERE isImportant = 1 AND isDeleted = 0 ORDER BY sortOrder ASC, createdAt DESC")
    fun getImportantTasks(): Flow<List<Task>>
}
