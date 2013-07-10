package com.marakana.android.yamba.svc;

import java.util.Iterator;
import java.util.List;

import com.marakana.android.yamba.R;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class YambaService extends IntentService {
	private static final String TAG = "SVC";
	private static final int OP_POST_COMPLETE = -1;
	private static final int OP_POST = -2;
	private static final int OP_POLL = -3;
	private static final String PARAM_STATUS = "YambaService.STATUS";
	private static final String PARAM_OP = "YambaService.OP";
	private static final long POLL_INTERVAL = 10 * 1000;
	
	public static class Hdlr extends Handler {
		private final YambaService svc;
		
		public Hdlr(YambaService svc) { this.svc = svc; }
		
		// ui thread
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case OP_POST_COMPLETE:
				svc.postComplete(msg.arg1);
				break;
			}
		}
	}
	
	public static void startPolling(Context ctxt) {
		Intent i = new Intent(ctxt, YambaService.class);
		i.putExtra(PARAM_OP, OP_POLL);
		
		AlarmManager svc = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
		svc.setInexactRepeating(
				AlarmManager.RTC, // if the application is not running it'll start running
				System.currentTimeMillis() + 100,
				POLL_INTERVAL,
				PendingIntent.getService(ctxt, OP_POLL, i, PendingIntent.FLAG_UPDATE_CURRENT));
	}
	
	public static void post(Context ctxt, String status) {
		Intent i = new Intent(ctxt, YambaService.class);
		i.putExtra(PARAM_STATUS, status);
		i.putExtra(PARAM_OP, OP_POST);
		ctxt.startService(i);
	}
	
	private volatile YambaClient yamba;
	private Hdlr hdlr;
	
	public YambaService() { super(TAG); }
	
	// ui thread
	public void onCreate() {
		super.onCreate();
		hdlr = new Hdlr(this);
        yamba = new YambaClient("student", "password", "http://yamba.marakana.com/api");
	}

	@Override
	protected void onHandleIntent(Intent i) {
		switch (i.getIntExtra(PARAM_OP, 0)) {
			case OP_POST:
				apiPost(i);
				break;

			case OP_POLL:
				apiPoll(i);
				break;
		}
	}

	private void apiPoll(Intent intent) {
		Log.d(TAG, "polling");
		try {
	    	List<Status> timeline = yamba.getTimeline(3);
			Iterator<Status> iterator = timeline.iterator();
			while (iterator.hasNext()) {
				Status status = iterator.next();
				Log.d(TAG, status.getMessage());
			}
    	} catch (YambaClientException e) {
    		Log.d(TAG, e.getMessage());
    		e.printStackTrace();
    	}
	}

	private void apiPost(Intent intent) {
		String statusMsg = intent.getStringExtra(PARAM_STATUS);
    	int msg = R.string.post_failed;
    	try {
    		yamba.postStatus(statusMsg);
    		msg = R.string.post_succeeded;
    	} catch (YambaClientException e) {
    		Log.d(TAG, e.getMessage());
    		e.printStackTrace();
    	}
		Message.obtain(hdlr, OP_POST_COMPLETE, msg, 0).sendToTarget();
	}
    
    void postComplete(int msg) {
    	Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
//    	if (msg == R.string.post_succeeded) {
//    		status.setText("");
//    	}
    }

}
