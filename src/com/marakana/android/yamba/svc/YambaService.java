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

public class YambaService extends IntentService {

  private static final String LOG_TAG = YambaService.class.getSimpleName();
  private static final String STATUS_PARAM = "YambaService.STATUS";
  private static final int OP_POST_COMPLETE = -1;

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

  private volatile YambaClient yamba;
  private YambaHandler handler;

  public YambaService() {
    super(LOG_TAG);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    yamba = new YambaClient("student", "password", "http://yamba.marakana.com/api");
    handler = new YambaHandler(this);
  }

  public static void post(Context c, String status) {
    Intent i = new Intent(c, YambaService.class);
    i.putExtra(STATUS_PARAM, status);
    c.startService(i);
  }

  // This is the only thing that runs outside the UI thread
  @Override
  protected void onHandleIntent(Intent i) {
    String status = i.getStringExtra(STATUS_PARAM);

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
