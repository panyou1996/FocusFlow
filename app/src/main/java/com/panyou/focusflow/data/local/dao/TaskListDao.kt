package com.panyou.focusflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.panyou.focusflow.data.local.entity.TaskList
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskListDao {
    @Query("SELECT * FROM task_lists WHERE isDeleted = 0 ORDER BY sortOrder ASC, createdAt ASC")
    fun getAllLists(): Flow<List<TaskList>>

    @Query("SELECT * FROM task_lists WHERE id = :listId LIMIT 1")
    suspend fun getListById(listId: String): TaskList?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(taskList: TaskList)

    @Update
    suspend fun updateList(taskList: TaskList)

    @Query("UPDATE task_lists SET isDeleted = 1 WHERE id = :listId")
    suspend fun softDeleteList(listId: String)
}
