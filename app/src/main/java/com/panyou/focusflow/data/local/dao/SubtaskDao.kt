package com.panyou.focusflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.panyou.focusflow.data.local.entity.Subtask
import kotlinx.coroutines.flow.Flow

@Dao
interface SubtaskDao {
    @Query("SELECT * FROM subtasks WHERE taskId = :taskId AND isDeleted = 0 ORDER BY sortOrder ASC, createdAt ASC")
    fun getSubtasksForTask(taskId: String): Flow<List<Subtask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtasks(subtasks: List<Subtask>)

    @Update
    suspend fun updateSubtask(subtask: Subtask)

    @Query("UPDATE subtasks SET isDeleted = 1 WHERE id = :subtaskId")
    suspend fun softDeleteSubtask(subtaskId: String)
}
