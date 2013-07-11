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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class YambaDbHelper extends SQLiteOpenHelper {
    public static String DATABASE = "yamba.db";
    public static int VERSION = 1;


    public static String TABLE_TIMELINE = "timeline";
    public static String COL_ID = "id";
    public static String COL_USER = "user";
    public static String COL_STATUS = "status";
    public static String COL_CREATED_AT = "created_at";



    public YambaDbHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_TIMELINE + " ("
                    + COL_ID + " INTEGER PRIMARY KEY,"
                    + COL_USER + " TEXT,"
                    + COL_STATUS + " TEXT,"
                    + COL_CREATED_AT + " INTEGER)"
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVers, int newVers) {
        db.execSQL("DROP TABLE " + TABLE_TIMELINE);
        onCreate(db);
    }

}
