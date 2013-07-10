package com.marakana.android.yamba.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class YambaDbHelper extends SQLiteOpenHelper {

  private static final String DATABASE       = "yamba.db";
  private static final int    VERSION        = 1;

  public static final String  TABLE_TIMELINE = "timeline";
  public static final String  COL_ID         = "id";
  public static final String  COL_USER       = "user";
  public static final String  COL_STATUS     = "status";
  public static final String  COL_CREATED_AT = "created_at";

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
        ");");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Only safe to drop since it's simply a cache
    db.execSQL("DROP TABLE " + TABLE_TIMELINE);
    onCreate(db);
  }

}
