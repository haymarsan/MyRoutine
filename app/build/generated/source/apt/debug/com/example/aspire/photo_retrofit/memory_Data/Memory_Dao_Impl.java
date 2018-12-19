package com.example.aspire.photo_retrofit.memory_Data;

import android.arch.lifecycle.ComputableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.InvalidationTracker.Observer;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import android.support.annotation.NonNull;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Memory_Dao_Impl implements Memory_Dao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfMemory_Model;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfMemory_Model;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMemory;

  public Memory_Dao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMemory_Model = new EntityInsertionAdapter<Memory_Model>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Memory`(`id`,`content`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Memory_Model value) {
        stmt.bindLong(1, value.getId());
        if (value.getContent() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getContent());
        }
      }
    };
    this.__deletionAdapterOfMemory_Model = new EntityDeletionOrUpdateAdapter<Memory_Model>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Memory` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Memory_Model value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__preparedStmtOfUpdateMemory = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update Memory Set content=? where id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertMemory(Memory_Model model) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfMemory_Model.insert(model);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteMemory(Memory_Model model) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfMemory_Model.handle(model);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateMemory(String content, int id) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMemory.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (content == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, content);
      }
      _argIndex = 2;
      _stmt.bindLong(_argIndex, id);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateMemory.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Memory_Model>> getAllMemory() {
    final String _sql = "Select * From Memory";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<Memory_Model>>() {
      private Observer _observer;

      @Override
      protected List<Memory_Model> compute() {
        if (_observer == null) {
          _observer = new Observer("Memory") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfContent = _cursor.getColumnIndexOrThrow("content");
          final List<Memory_Model> _result = new ArrayList<Memory_Model>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Memory_Model _item;
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            _item = new Memory_Model(_tmpContent);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
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
    }.getLiveData();
  }
}
