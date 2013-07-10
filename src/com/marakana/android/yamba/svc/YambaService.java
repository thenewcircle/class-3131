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

public class YambaService extends IntentService {

  private static final String YAMBA_API_URL = "http://yamba.marakana.com/api";
  private static final String YAMBA_USERNAME = "student";
  private static final String YAMBA_PASSWORD = "password";

  private static final String LOG_TAG = YambaService.class.getSimpleName();

  private static final String PARAM_OP = "YambaService.OP";
  private static final String PARAM_STATUS = "YambaService.STATUS";

  private static final long POLL_SECS = 10;

  private static final int OP_POST_COMPLETE = -1;
  private static final int OP_POST = -2;
  private static final int OP_POLL = -3;

  private static final int MAX_POSTS = 20;

  private volatile YambaClient yamba;
  private YambaHandler handler;

  private static class YambaHandler extends Handler {

    private final YambaService svc;

    public YambaHandler(YambaService svc) {
      this.svc = svc;
    }

    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
      case OP_POST_COMPLETE:
        svc.postComplete(msg.arg1);
        break;
      }
    }
  }

  public YambaService() {
    super(LOG_TAG);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    yamba = new YambaClient(YAMBA_USERNAME, YAMBA_PASSWORD, YAMBA_API_URL);
    handler = new YambaHandler(this);
  }

  public static void startPolling(Context c) {
    Intent i = new Intent(c, YambaService.class);
    i.putExtra(PARAM_OP, OP_POLL);
    AlarmManager svc = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
    svc.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 100, POLL_SECS * 1000,
        PendingIntent.getService(c, OP_POLL, i, PendingIntent.FLAG_NO_CREATE));
  }

  public static void post(Context c, String status) {
    Intent i = new Intent(c, YambaService.class);
    i.putExtra(PARAM_STATUS, status);
    i.putExtra(PARAM_OP, OP_POST);
    c.startService(i);
  }

  // This is the only thing that runs outside the UI thread
  @Override
  protected void onHandleIntent(Intent i) {
    switch (i.getIntExtra(PARAM_OP, 0)) {
    case OP_POST:
      postStatus(i);
      break;
    case OP_POLL:
      fetchTimeline();
      break;
    }
  }

  private void fetchTimeline() {
    try {
      List<Status> statuses = yamba.getTimeline(MAX_POSTS);
      for (Status status : statuses) {
        Log.d(LOG_TAG, status.getUser() + ": " + status.getMessage());
      }
    } catch (YambaClientException e) {
      Log.e(LOG_TAG, "Failed to fetch timeline", e);
    }
  }

  private void postStatus(Intent i) {
    String status = i.getStringExtra(PARAM_STATUS);

    int msg = R.string.post_failed;
    try {
      yamba.postStatus(status);
      msg = R.string.post_succeeded;
    } catch (YambaClientException e) {
      Log.e(LOG_TAG, "Post failed: ", e);
    }

    Message.obtain(handler, OP_POST_COMPLETE, msg, 0).sendToTarget();
  }

  private void postComplete(int msg) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }

}
