package com.example.aspire.photo_retrofit.Money_Data;

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

public class Money_Dao_Impl implements Money_Dao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfMoney_Model;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfMoney_Model;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMoney;

  public Money_Dao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMoney_Model = new EntityInsertionAdapter<Money_Model>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Money`(`id`,`date`,`content`,`income`,`outcome`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Money_Model value) {
        stmt.bindLong(1, value.getId());
        if (value.getDate() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDate());
        }
        if (value.getContent() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getContent());
        }
        if (value.getIncome() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getIncome());
        }
        if (value.getOutcome() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getOutcome());
        }
      }
    };
    this.__deletionAdapterOfMoney_Model = new EntityDeletionOrUpdateAdapter<Money_Model>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Money` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Money_Model value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__preparedStmtOfUpdateMoney = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update Money Set content=? ,date=?,income=?,outcome=? where id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertMoney(Money_Model model) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfMoney_Model.insert(model);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteMoney(Money_Model model) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfMoney_Model.handle(model);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateMoney(String content, String date, String income, String outcome, int id) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMoney.acquire();
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
      if (income == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, income);
      }
      _argIndex = 4;
      if (outcome == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, outcome);
      }
      _argIndex = 5;
      _stmt.bindLong(_argIndex, id);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateMoney.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Money_Model>> getAllMoney() {
    final String _sql = "Select * From Money";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<Money_Model>>() {
      private Observer _observer;

      @Override
      protected List<Money_Model> compute() {
        if (_observer == null) {
          _observer = new Observer("Money") {
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
          final int _cursorIndexOfDate = _cursor.getColumnIndexOrThrow("date");
          final int _cursorIndexOfContent = _cursor.getColumnIndexOrThrow("content");
          final int _cursorIndexOfIncome = _cursor.getColumnIndexOrThrow("income");
          final int _cursorIndexOfOutcome = _cursor.getColumnIndexOrThrow("outcome");
          final List<Money_Model> _result = new ArrayList<Money_Model>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Money_Model _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpIncome;
            _tmpIncome = _cursor.getString(_cursorIndexOfIncome);
            final String _tmpOutcome;
            _tmpOutcome = _cursor.getString(_cursorIndexOfOutcome);
            _item = new Money_Model(_tmpContent,_tmpDate,_tmpIncome,_tmpOutcome);
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
