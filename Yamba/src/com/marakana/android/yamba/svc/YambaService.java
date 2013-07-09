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
package com.marakana.android.yamba.svc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.R;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class YambaService extends IntentService {
    private static final String TAG = "SVC";

    static final int OP_POST_COMPLETE = -1;

    private static final String PARAM_STATUS = "YambaService.STATUS";

    private static class Hdlr extends Handler {
        private final  YambaService svc;

        public Hdlr(YambaService svc) { this.svc = svc; }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OP_POST_COMPLETE:
                    svc.postComplete(msg.arg1);
                    break;
            }
        }
    }

    public static void post(Context ctxt, String statusMsg) {
        Intent i = new Intent(ctxt, YambaService.class);
        i.putExtra(PARAM_STATUS, statusMsg);
        ctxt.startService(i);
    }


    private volatile YambaClient yamba;
    private Hdlr hdlr;

    public YambaService() { super(TAG); }

    @Override
    public void onCreate() {
        super.onCreate();
        hdlr = new Hdlr(this);
        yamba = new YambaClient("student", "password", "http://yamba.marakana.com/api");
    }

    @Override
    protected void onHandleIntent(Intent i) {
        String statusMsg = i.getStringExtra(PARAM_STATUS);

        int msg = R.string.post_failed;
        try {
            yamba.postStatus(statusMsg);
            msg = R.string.post_succeeded;
        }
        catch (YambaClientException e) {
            Log.e(TAG, "Post failed: " + e, e);
        }

        Message.obtain(hdlr, OP_POST_COMPLETE, msg, 0).sendToTarget();
    }

    public void postComplete(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
