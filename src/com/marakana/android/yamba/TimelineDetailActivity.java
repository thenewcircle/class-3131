package com.marakana.android.yamba;

import android.os.Bundle;

public class TimelineDetailActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline_detail);

    if (savedInstanceState == null) savedInstanceState = getIntent().getExtras();

    // Intent could be missing extras, so this extra check is needed
    if (savedInstanceState != null) {
      TimelineDetailFragment details = (TimelineDetailFragment) getFragmentManager()
              .findFragmentById(R.id.fragment_timeline_detail);
      details.setContent(savedInstanceState);
    }
  }

}
