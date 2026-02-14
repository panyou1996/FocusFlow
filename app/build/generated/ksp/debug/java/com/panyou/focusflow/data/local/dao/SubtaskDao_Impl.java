package com.panyou.focusflow.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.panyou.focusflow.data.local.entity.Subtask;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SubtaskDao_Impl implements SubtaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Subtask> __insertionAdapterOfSubtask;

  private final EntityDeletionOrUpdateAdapter<Subtask> __updateAdapterOfSubtask;

  private final SharedSQLiteStatement __preparedStmtOfSoftDeleteSubtask;

  public SubtaskDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSubtask = new EntityInsertionAdapter<Subtask>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `subtasks` (`id`,`taskId`,`title`,`isCompleted`,`sortOrder`,`createdAt`,`updatedAt`,`isDeleted`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Subtask entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTaskId());
        statement.bindString(3, entity.getTitle());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getSortOrder());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        final int _tmp_1 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
      }
    };
    this.__updateAdapterOfSubtask = new EntityDeletionOrUpdateAdapter<Subtask>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `subtasks` SET `id` = ?,`taskId` = ?,`title` = ?,`isCompleted` = ?,`sortOrder` = ?,`createdAt` = ?,`updatedAt` = ?,`isDeleted` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Subtask entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTaskId());
        statement.bindString(3, entity.getTitle());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getSortOrder());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        final int _tmp_1 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        statement.bindString(9, entity.getId());
      }
    };
    this.__preparedStmtOfSoftDeleteSubtask = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE subtasks SET isDeleted = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSubtasks(final List<Subtask> subtasks,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSubtask.insert(subtasks);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSubtask(final Subtask subtask, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSubtask.handle(subtask);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object softDeleteSubtask(final String subtaskId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDeleteSubtask.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, subtaskId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSoftDeleteSubtask.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Subtask>> getSubtasksForTask(final String taskId) {
    final String _sql = "SELECT * FROM subtasks WHERE taskId = ? AND isDeleted = 0 ORDER BY sortOrder ASC, createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, taskId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"subtasks"}, new Callable<List<Subtask>>() {
      @Override
      @NonNull
      public List<Subtask> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final List<Subtask> _result = new ArrayList<Subtask>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Subtask _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTaskId;
            _tmpTaskId = _cursor.getString(_cursorIndexOfTaskId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final boolean _tmpIsDeleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_1 != 0;
            _item = new Subtask(_tmpId,_tmpTaskId,_tmpTitle,_tmpIsCompleted,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
