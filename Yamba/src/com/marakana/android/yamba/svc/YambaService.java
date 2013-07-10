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

import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.R;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class YambaService extends IntentService {
    private static final String TAG = "SVC";

    static final int OP_POST_COMPLETE = -1;
    static final int OP_POST = -2;
    static final int OP_POLL = -3;

    private static final String PARAM_OP = "YambaService.OP";
    private static final String PARAM_STATUS = "YambaService.STATUS";

    private static final long POLL_INTERVAL = 10 * 1000;
    private static final int MAX_POLLS = 20;

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

    public static void stopPolling(Context ctxt) {
        ((AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE))
            .cancel(createPollingIntent(ctxt));
    }

    public static void startPolling(Context ctxt) {
        ((AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE))
            .setInexactRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis() + 100,
                POLL_INTERVAL,
                createPollingIntent(ctxt));
    }

    public static void post(Context ctxt, String statusMsg) {
        Intent i = new Intent(ctxt, YambaService.class);
        i.putExtra(PARAM_OP, OP_POST);
        i.putExtra(PARAM_STATUS, statusMsg);
        ctxt.startService(i);
    }

    private static PendingIntent createPollingIntent(Context ctxt) {
        Intent i = new Intent(ctxt, YambaService.class);
        i.putExtra(PARAM_OP, OP_POLL);
        return PendingIntent.getService(ctxt, OP_POLL, i, PendingIntent.FLAG_UPDATE_CURRENT);
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
        switch (i.getIntExtra(PARAM_OP, 0)) {
            case OP_POST:
                doPost(i.getStringExtra(PARAM_STATUS));
                break;
            case OP_POLL:
                doPoll();
                break;
        }
    }

    private void doPost(String statusMsg) {
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

    void postComplete(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void doPoll() {
        Log.d(TAG, "polling");
        List<Status> timeline;
        try { timeline = yamba.getTimeline(MAX_POLLS); }
        catch (YambaClientException e) {
            Log.e(TAG, "Post failed: " + e, e);
            return;
        }
        for (Status status: timeline) {
            Log.d(TAG, "Status: " + status.getUser() + ": " + status.getMessage());
        }
    }
}
