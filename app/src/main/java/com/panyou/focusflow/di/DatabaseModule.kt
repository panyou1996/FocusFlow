package com.panyou.focusflow.di

import android.content.Context
import androidx.room.Room
import com.panyou.focusflow.data.local.AppDatabase
import com.panyou.focusflow.data.local.dao.SubtaskDao
import com.panyou.focusflow.data.local.dao.TaskDao
import com.panyou.focusflow.data.local.dao.TaskListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "focus_flow_db"
        )
        .fallbackToDestructiveMigration() // For dev simplicity
        .build()
    }

    @Provides
    fun provideTaskListDao(db: AppDatabase): TaskListDao = db.taskListDao()

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideSubtaskDao(db: AppDatabase): SubtaskDao = db.subtaskDao()
}
