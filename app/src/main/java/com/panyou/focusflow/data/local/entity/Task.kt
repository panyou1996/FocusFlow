package com.panyou.focusflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = TaskList::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("listId")]
)
data class Task(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val listId: String,
    val title: String,
    val isCompleted: Boolean = false,
    val isImportant: Boolean = false,
    val reminderTime: Long? = null,
    val dueDate: Long? = null,
    val noteContent: String? = null,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)
