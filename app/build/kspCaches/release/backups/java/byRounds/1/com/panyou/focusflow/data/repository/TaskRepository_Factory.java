package com.panyou.focusflow.data.repository;

import com.panyou.focusflow.data.local.dao.SubtaskDao;
import com.panyou.focusflow.data.local.dao.TaskDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class TaskRepository_Factory implements Factory<TaskRepository> {
  private final Provider<TaskDao> taskDaoProvider;

  private final Provider<SubtaskDao> subtaskDaoProvider;

  public TaskRepository_Factory(Provider<TaskDao> taskDaoProvider,
      Provider<SubtaskDao> subtaskDaoProvider) {
    this.taskDaoProvider = taskDaoProvider;
    this.subtaskDaoProvider = subtaskDaoProvider;
  }

  @Override
  public TaskRepository get() {
    return newInstance(taskDaoProvider.get(), subtaskDaoProvider.get());
  }

  public static TaskRepository_Factory create(Provider<TaskDao> taskDaoProvider,
      Provider<SubtaskDao> subtaskDaoProvider) {
    return new TaskRepository_Factory(taskDaoProvider, subtaskDaoProvider);
  }

  public static TaskRepository newInstance(TaskDao taskDao, SubtaskDao subtaskDao) {
    return new TaskRepository(taskDao, subtaskDao);
  }
}
