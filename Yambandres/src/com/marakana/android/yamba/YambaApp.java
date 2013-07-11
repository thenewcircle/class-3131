package com.marakana.android.yamba;

import android.app.Application;
import android.util.Log;

import com.marakana.android.yamba.svc.YambaService;

public class YambaApp extends Application {

  private static final String LOG_TAG = YambaApp.class.getSimpleName();

  @Override
  public void onCreate() {
    Log.d(LOG_TAG, "YambaApp started");
    super.onCreate();
    YambaService.startPolling(this);
  }

}
