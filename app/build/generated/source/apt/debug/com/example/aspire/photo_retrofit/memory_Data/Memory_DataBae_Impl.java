package com.example.aspire.photo_retrofit.memory_Data;

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

public class Memory_DataBae_Impl extends Memory_DataBae {
  private volatile Memory_Dao _memoryDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Memory` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `content` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"f03dbcae988be5af4284940f06138299\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Memory`");
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
        final HashMap<String, TableInfo.Column> _columnsMemory = new HashMap<String, TableInfo.Column>(2);
        _columnsMemory.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsMemory.put("content", new TableInfo.Column("content", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMemory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMemory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMemory = new TableInfo("Memory", _columnsMemory, _foreignKeysMemory, _indicesMemory);
        final TableInfo _existingMemory = TableInfo.read(_db, "Memory");
        if (! _infoMemory.equals(_existingMemory)) {
          throw new IllegalStateException("Migration didn't properly handle Memory(com.example.aspire.photo_retrofit.memory_Data.Memory_Model).\n"
                  + " Expected:\n" + _infoMemory + "\n"
                  + " Found:\n" + _existingMemory);
        }
      }
    }, "f03dbcae988be5af4284940f06138299");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Memory");
  }

  @Override
  public Memory_Dao data() {
    if (_memoryDao != null) {
      return _memoryDao;
    } else {
      synchronized(this) {
        if(_memoryDao == null) {
          _memoryDao = new Memory_Dao_Impl(this);
        }
        return _memoryDao;
      }
    }
  }
}
