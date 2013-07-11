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
package com.marakana.android.yamba;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class YambaContract {
    private YambaContract() { }

    public static final int VERSION = 1;

    public static final String AUTHORITY = "com.marakana.android.yamba.timeline";

    public static final Uri BASE_URI = new Uri.Builder()
    .scheme(ContentResolver.SCHEME_CONTENT)
    .authority(AUTHORITY)
    .build();

    private static final String MINOR_TYPE = "/vnd." + AUTHORITY;

    public static final String ITEM_TYPE
    = ContentResolver.CURSOR_ITEM_BASE_TYPE + MINOR_TYPE;
    public static final String DIR_TYPE
    = ContentResolver.CURSOR_DIR_BASE_TYPE + MINOR_TYPE;

    public static class Timeline {
        private Timeline() { }

        public static final String TABLE = "timeline";

        public static final Uri URI = BASE_URI.buildUpon().appendPath(TABLE).build();

        public static class Columns {
            public static String ID = BaseColumns._ID;
            public static String USER = "user";
            public static String STATUS = "status";
            public static String CREATED_AT = "created_at";

            public static String MAX_TIMESTAMP = "maxts";
        }
    }
}
