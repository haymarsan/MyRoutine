package com.example.aspire.photo_retrofit.Money_Data;

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

public class Money_Database_Impl extends Money_Database {
  private volatile Money_Dao _moneyDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Money` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` TEXT, `content` TEXT, `income` TEXT, `outcome` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"30e6ec674bcc0e33608763a34cebabdd\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Money`");
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
        final HashMap<String, TableInfo.Column> _columnsMoney = new HashMap<String, TableInfo.Column>(5);
        _columnsMoney.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsMoney.put("date", new TableInfo.Column("date", "TEXT", false, 0));
        _columnsMoney.put("content", new TableInfo.Column("content", "TEXT", false, 0));
        _columnsMoney.put("income", new TableInfo.Column("income", "TEXT", false, 0));
        _columnsMoney.put("outcome", new TableInfo.Column("outcome", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMoney = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMoney = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMoney = new TableInfo("Money", _columnsMoney, _foreignKeysMoney, _indicesMoney);
        final TableInfo _existingMoney = TableInfo.read(_db, "Money");
        if (! _infoMoney.equals(_existingMoney)) {
          throw new IllegalStateException("Migration didn't properly handle Money(com.example.aspire.photo_retrofit.Money_Data.Money_Model).\n"
                  + " Expected:\n" + _infoMoney + "\n"
                  + " Found:\n" + _existingMoney);
        }
      }
    }, "30e6ec674bcc0e33608763a34cebabdd");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Money");
  }

  @Override
  public Money_Dao data() {
    if (_moneyDao != null) {
      return _moneyDao;
    } else {
      synchronized(this) {
        if(_moneyDao == null) {
          _moneyDao = new Money_Dao_Impl(this);
        }
        return _moneyDao;
      }
    }
  }
}
