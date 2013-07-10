package com.marakana.android.yamba.svc;

import com.marakana.android.yamba.R;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class YambaService extends IntentService {
	private static final String TAG = "SVC";
	private static final int OP_POST_COMPLETE = -1;
	private static final String PARAM_STATUS = "YambaService.status";
	
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
	
	public static void post(Context ctxt, String status) {
		Intent i = new Intent(ctxt, YambaService.class);
		i.putExtra(PARAM_STATUS, status);
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
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
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
