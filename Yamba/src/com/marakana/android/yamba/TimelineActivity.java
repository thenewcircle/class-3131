package com.marakana.android.yamba;

import android.os.Bundle;

import com.marakana.android.yamba.svc.YambaService;


public class TimelineActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
    }

    @Override
    protected void onPause() {
        super.onPause();
        YambaService.stopPolling(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        YambaService.startPolling(this);
    }
}
