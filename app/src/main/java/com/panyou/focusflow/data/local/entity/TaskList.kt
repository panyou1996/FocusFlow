package com.panyou.focusflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "task_lists")
data class TaskList(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val iconName: String? = null,
    val colorHex: String? = null,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)
