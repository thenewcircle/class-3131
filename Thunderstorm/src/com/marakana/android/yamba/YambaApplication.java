package com.marakana.android.yamba;

import com.marakana.android.yamba.svc.YambaService;

import android.app.Application;
import android.util.Log;

public class YambaApplication extends Application {

    private static final String TAG = "YAP";

	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Yamba polling started.");
		YambaService.startPolling(this);
	}
}
