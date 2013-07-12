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
  public void onResume() {
    super.onResume();
    YambaService.startPolling(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    YambaService.stopPolling(this);
  }

}
