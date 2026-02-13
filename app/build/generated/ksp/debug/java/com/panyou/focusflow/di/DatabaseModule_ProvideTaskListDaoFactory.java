package com.panyou.focusflow.di;

import com.panyou.focusflow.data.local.AppDatabase;
import com.panyou.focusflow.data.local.dao.TaskListDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvideTaskListDaoFactory implements Factory<TaskListDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideTaskListDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TaskListDao get() {
    return provideTaskListDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideTaskListDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideTaskListDaoFactory(dbProvider);
  }

  public static TaskListDao provideTaskListDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTaskListDao(db));
  }
}
