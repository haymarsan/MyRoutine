package com.example.aspire.photo_retrofit.Data;

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

public class MyDao_Impl implements MyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfModel;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfModel;

  private final SharedSQLiteStatement __preparedStmtOfUpdateEvent;

  public MyDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfModel = new EntityInsertionAdapter<Model>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Event`(`id`,`content`,`date`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Model value) {
        stmt.bindLong(1, value.getId());
        if (value.getContent() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getContent());
        }
        if (value.getDate() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDate());
        }
      }
    };
    this.__deletionAdapterOfModel = new EntityDeletionOrUpdateAdapter<Model>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Event` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Model value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__preparedStmtOfUpdateEvent = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update Event Set content=?,date=? where id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertEvent(Model model) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfModel.insert(model);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteEvent(Model model) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfModel.handle(model);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateEvent(String content, String date, int id) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateEvent.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (content == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, content);
      }
      _argIndex = 2;
      if (date == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, date);
      }
      _argIndex = 3;
      _stmt.bindLong(_argIndex, id);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateEvent.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Model>> getAllEvent() {
    final String _sql = "Select * From Event";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<Model>>() {
      private Observer _observer;

      @Override
      protected List<Model> compute() {
        if (_observer == null) {
          _observer = new Observer("Event") {
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
          final int _cursorIndexOfDate = _cursor.getColumnIndexOrThrow("date");
          final List<Model> _result = new ArrayList<Model>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Model _item;
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            _item = new Model(_tmpContent,_tmpDate);
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
