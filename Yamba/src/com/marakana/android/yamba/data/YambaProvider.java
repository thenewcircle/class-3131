package com.marakana.android.yamba.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.marakana.android.yamba.YambaContract;


public class YambaProvider extends ContentProvider {
    private static final String TAG = "PROVIDER";

    //  scheme     authority                   path      [id]
    // content://com.marakana.android.yamba/contactphone/7
    private static final int TIMELINE_ITEM_TYPE = 1;
    private static final int TIMELINE_DIR_TYPE = 2;
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        MATCHER.addURI(
                YambaContract.AUTHORITY,
                YambaContract.Timeline.TABLE + "/#",
                TIMELINE_ITEM_TYPE);
        MATCHER.addURI(
                YambaContract.AUTHORITY,
                YambaContract.Timeline.TABLE,
                TIMELINE_DIR_TYPE);
    }

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
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)) {
            case TIMELINE_ITEM_TYPE:
                return YambaContract.ITEM_TYPE;
            case TIMELINE_DIR_TYPE:
                return YambaContract.DIR_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] proj, String sel, String[] selArgs, String sort) {
        Log.d(TAG, "query");

        long pk = -1;
        switch (MATCHER.match(uri)) {
            case TIMELINE_ITEM_TYPE:
                pk = ContentUris.parseId(uri);
                break;
            case TIMELINE_DIR_TYPE:
                break;
            default:
                throw new IllegalArgumentException("Unrecognized URI: " + uri);
        }

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(YambaDbHelper.TABLE_TIMELINE);
        qb.setProjectionMap(PROJ_MAP_TIMELINE.getProjectionMap());

        if (0 < pk) { qb.appendWhere(YambaDbHelper.COL_ID +"=" + pk); }

        Cursor c = qb.query(getDb(), proj, sel, selArgs, null, null, sort);

        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] rows) {
        Log.d(TAG, "insert: " + rows.length);

        switch (MATCHER.match(uri)) {
            case TIMELINE_ITEM_TYPE:
            case TIMELINE_DIR_TYPE:
                break;
            default:
                throw new IllegalArgumentException("Unrecognized URI: " + uri);
        }

        int count = 0;
        SQLiteDatabase db = getDb();
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

        if (0 < count) {
            getContext().getContentResolver().notifyChange(uri, null);
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

    private SQLiteDatabase getDb() { return dbHelper.getWritableDatabase(); }
}
