/* $Id: $
   Copyright 2013, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.marakana.android.yamba.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.marakana.android.yamba.YambaContract;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class YambaProvider extends ContentProvider {


    //  SELECT baz AS foo
    //     FROM bar
    //     ORDER BY foo

    // foo => baz
    private static final ColumnMap COL_MAP_TIMELINE = new ColumnMap.Builder()
        .addColumn(YambaContract.Timeline.Columns.ID, YambaDbHelper.COL_ID, ColumnMap.Type.LONG)
        .addColumn(YambaContract.Timeline.Columns.CREATED_AT, YambaDbHelper.COL_CREATED_AT, ColumnMap.Type.LONG)
        .addColumn(YambaContract.Timeline.Columns.USER, YambaDbHelper.COL_USER, ColumnMap.Type.STRING)
        .addColumn(YambaContract.Timeline.Columns.STATUS, YambaDbHelper.COL_STATUS, ColumnMap.Type.STRING)
        .build();

    private static final ProjectionMap PROJ_MAP_TIMELINE = new ProjectionMap.Builder()
        .addColumn(YambaContract.Timeline.Columns.ID, YambaDbHelper.COL_ID)
        .addColumn(YambaContract.Timeline.Columns.CREATED_AT, YambaDbHelper.COL_CREATED_AT)
        .addColumn(YambaContract.Timeline.Columns.USER, YambaDbHelper.COL_USER)
        .addColumn(YambaContract.Timeline.Columns.STATUS, YambaDbHelper.COL_STATUS)
        .addColumn(YambaContract.Timeline.Columns.MAX_TIMESTAMP, "max(" + YambaDbHelper.COL_CREATED_AT + ")")
        .build();

    YambaDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new YambaDbHelper(getContext());
        return null != dbHelper;
    }

    @Override
    public String getType(Uri arg0) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] proj, String sel, String[] selArgs, String sort) {
         SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
         qb.setTables(YambaDbHelper.TABLE_TIMELINE);
         qb.setProjectionMap(PROJ_MAP_TIMELINE.getProjectionMap());

         return qb.query(dbHelper.getWritableDatabase(), proj, sel, selArgs, null, null, sort);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] rows) {
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
