package com.marakana.android.yamba;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.marakana.android.yamba.svc.YambaService;

public class TimelineActivity extends BaseActivity {

  private static final String TAG_DETAIL = "DETAIL_FRAGMENT";
  private boolean usingFragments = true;

  @Override
  public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode,
          Bundle options) {
    if (!usingFragments) super.startActivityFromFragment(fragment, intent, requestCode, options);
    else launchDetailFragment(intent.getStringExtra(TimelineDetailFragment.PARAM_STATUS));
  }

  private void launchDetailFragment(String status) {
    FragmentTransaction xact = getFragmentManager().beginTransaction();
    xact.replace(R.id.timeline_detail, TimelineDetailFragment.newInstance(status), TAG_DETAIL);
    xact.addToBackStack(null);
    xact.commit();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);

    usingFragments = findViewById(R.id.timeline_detail) != null;

    if (usingFragments) addDetailFragment();
  }

  private void addDetailFragment() {
    FragmentManager mgr = getFragmentManager();

    // Sreen was rotated, so fragment was already there
    if (mgr.findFragmentByTag(TAG_DETAIL) != null) return;

    FragmentTransaction xact = mgr.beginTransaction();
    xact.add(R.id.timeline_detail, new TimelineDetailFragment(), TAG_DETAIL);
    xact.commit();
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
