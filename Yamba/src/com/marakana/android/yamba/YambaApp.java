package com.marakana.android.yamba;

import com.marakana.android.yamba.svc.YambaService;

import android.app.Application;
import android.util.Log;

public class YambaApp extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		YambaService.startPolling(this);
		Log.d("APP", "app onCreate");
	}

}
