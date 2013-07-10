package com.marakana.android.yamba.svc;

import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.R;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;
import com.marakana.android.yamba.data.YambaDbHelper;

public class YambaService extends IntentService {

  private static final String  LOG_TAG          = YambaService.class.getSimpleName();

  private static final String  YAMBA_API_URL    = "http://yamba.marakana.com/api";
  private static final String  YAMBA_USERNAME   = "student";
  private static final String  YAMBA_PASSWORD   = "password";

  private static final String  PARAM_OP         = "YambaService.OP";
  private static final String  PARAM_STATUS     = "YambaService.STATUS";

  private static final long    POLL_SECS        = 10;

  private static final int     OP_POST_COMPLETE = -1;
  private static final int     OP_POST          = -2;
  private static final int     OP_POLL          = -3;

  private static final int     MAX_POSTS        = 20;

  private volatile YambaClient yamba;
  private YambaHandler         handler;
  private YambaDbHelper        dbHelper;

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
    dbHelper = new YambaDbHelper(this);
  }

  public static void startPolling(Context c) {
    getAlarmService(c).setInexactRepeating(
        AlarmManager.RTC,
        System.currentTimeMillis() + 100, // Need padding since operation may completer after NOW
        POLL_SECS * 1000,
        createPollingIntent(c));
  }

  public static void stopPolling(Context c) {
    getAlarmService(c).cancel(createPollingIntent(c));
  }

  private static AlarmManager getAlarmService(Context c) {
    return (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
  }

  private static PendingIntent createPollingIntent(Context c) {
    Intent i = new Intent(c, YambaService.class);
    i.putExtra(PARAM_OP, OP_POLL);
    return PendingIntent.getService(c, OP_POLL, i, PendingIntent.FLAG_UPDATE_CURRENT);
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
    List<Status> timeline = new ArrayList<Status>();

    try {
      timeline = yamba.getTimeline(MAX_POSTS);
    } catch (YambaClientException e) {
      Log.e(LOG_TAG, "Failed to fetch timeline", e);
    }

    SQLiteDatabase db = dbHelper.getWritableDatabase();
    for (Status status : timeline) {
      ContentValues cv = new ContentValues();
      cv.put(YambaDbHelper.COL_ID, status.getId());
      cv.put(YambaDbHelper.COL_USER, status.getUser());
      cv.put(YambaDbHelper.COL_STATUS, status.getMessage());
      cv.put(YambaDbHelper.COL_CREATED_AT, status.getCreatedAt().getTime());
      db.insert(YambaDbHelper.TABLE_TIMELINE, null, cv);
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
