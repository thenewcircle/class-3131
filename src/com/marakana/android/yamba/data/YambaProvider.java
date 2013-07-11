package com.marakana.android.yamba.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.marakana.android.yamba.YambaContract;


public class YambaProvider extends ContentProvider {
    private static final String TAG = "PROVIDER";

    private static final ColumnMap COL_MAP_TIMELINE = new ColumnMap.Builder()
        .addColumn(
                YambaContract.Timeline.Columns.ID,
                YambaDbHelper.COL_ID, ColumnMap.Type.LONG)
        .addColumn(
                YambaContract.Timeline.Columns.CREATED_AT,
                YambaDbHelper.COL_CREATED_AT, ColumnMap.Type.LONG)
        .addColumn(
                YambaContract.Timeline.Columns.USER,
                YambaDbHelper.COL_USER, ColumnMap.Type.STRING)
        .addColumn(
                YambaContract.Timeline.Columns.STATUS,
                YambaDbHelper.COL_STATUS, ColumnMap.Type.STRING)
        .build();

    private static final ProjectionMap PROJ_MAP_TIMELINE = new ProjectionMap.Builder()
        .addColumn(YambaContract.Timeline.Columns.ID, YambaDbHelper.COL_ID)
        .addColumn(YambaContract.Timeline.Columns.CREATED_AT, YambaDbHelper.COL_CREATED_AT)
        .addColumn(YambaContract.Timeline.Columns.USER, YambaDbHelper.COL_USER)
        .addColumn(YambaContract.Timeline.Columns.STATUS, YambaDbHelper.COL_STATUS)
        .addColumn(
                YambaContract.Timeline.Columns.MAX_TIMESTAMP,
                "max(" + YambaDbHelper.COL_CREATED_AT + ")")
        .build();


    private YambaDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "provider created");
        dbHelper = new YambaDbHelper(getContext());
        return null != dbHelper;
    }

    @Override
    public String getType(Uri arg0) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] proj, String sel, String[] selArgs, String sort) {
        Log.d(TAG, "query");

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(YambaDbHelper.TABLE_TIMELINE);
        qb.setProjectionMap(PROJ_MAP_TIMELINE.getProjectionMap());

        return qb.query(dbHelper.getWritableDatabase(), proj, sel, selArgs, null, null, sort);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] rows) {
        Log.d(TAG, "insert: " + rows.length);

        int count = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            for (ContentValues row: rows) {
                if (0 < db.insert(YambaDbHelper.TABLE_TIMELINE, null, COL_MAP_TIMELINE.translateCols(row))) {
                    count++;
                }
            }

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        return count;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        throw new UnsupportedOperationException("insert not supported");
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        throw new UnsupportedOperationException("update not supported");
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        throw new UnsupportedOperationException("delete not supported");
    }
}
