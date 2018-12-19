package com.example.aspire.photo_retrofit.Data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.HashSet;

public class MyDataBae_Impl extends MyDataBae {
  private volatile MyDao _myDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Event` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `content` TEXT, `date` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"bd4962c9e29c4d1d4a4176766474a663\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Event`");
      }

      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsEvent = new HashMap<String, TableInfo.Column>(3);
        _columnsEvent.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsEvent.put("content", new TableInfo.Column("content", "TEXT", false, 0));
        _columnsEvent.put("date", new TableInfo.Column("date", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEvent = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEvent = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEvent = new TableInfo("Event", _columnsEvent, _foreignKeysEvent, _indicesEvent);
        final TableInfo _existingEvent = TableInfo.read(_db, "Event");
        if (! _infoEvent.equals(_existingEvent)) {
          throw new IllegalStateException("Migration didn't properly handle Event(com.example.aspire.photo_retrofit.Data.Model).\n"
                  + " Expected:\n" + _infoEvent + "\n"
                  + " Found:\n" + _existingEvent);
        }
      }
    }, "bd4962c9e29c4d1d4a4176766474a663");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Event");
  }

  @Override
  public MyDao data() {
    if (_myDao != null) {
      return _myDao;
    } else {
      synchronized(this) {
        if(_myDao == null) {
          _myDao = new MyDao_Impl(this);
        }
        return _myDao;
      }
    }
  }
}
