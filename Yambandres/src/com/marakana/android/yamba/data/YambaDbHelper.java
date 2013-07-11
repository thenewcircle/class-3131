package com.marakana.android.yamba.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class YambaDbHelper extends SQLiteOpenHelper {
  private static final String LOG_TAG        = YambaDbHelper.class.getSimpleName();

  private static final String DATABASE       = "yamba.db";
  private static final int    VERSION        = 1;

  static final String         TABLE_TIMELINE = "timeline";
  static final String         COL_ID         = "id";
  static final String         COL_USER       = "user";
  static final String         COL_STATUS     = "status";
  static final String         COL_CREATED_AT = "created_at";

  public YambaDbHelper(Context context) {
    super(context, DATABASE, null, VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + TABLE_TIMELINE +
            " (" +
            COL_ID + " INTEGER PRIMARY KEY," +
            COL_USER + " TEXT," +
            COL_STATUS + " TEXT," +
            COL_CREATED_AT + " INTEGER" +
            ")");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.d(LOG_TAG, "updating db");
    // Only safe to drop since it's simply a cache
    db.execSQL("DROP TABLE " + TABLE_TIMELINE);
    onCreate(db);
  }

}
