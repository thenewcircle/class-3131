package com.marakana.android.yamba;

import android.app.Application;
import android.util.Log;

public class YambaApp extends Application {

  private static final String LOG_TAG = YambaApp.class.getSimpleName();

  @Override
  public void onCreate() {
    Log.d(LOG_TAG, "YambaApp started");
    super.onCreate();
  }

}
