package com.panyou.focusflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.panyou.focusflow.data.local.dao.TaskDao
import com.panyou.focusflow.data.local.dao.TaskListDao
import com.panyou.focusflow.data.local.entity.Subtask
import com.panyou.focusflow.data.local.entity.Task
import com.panyou.focusflow.data.local.entity.TaskList

@Database(
    entities = [TaskList::class, Task::class, Subtask::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskListDao(): TaskListDao
    abstract fun taskDao(): TaskDao
}
