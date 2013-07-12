package com.marakana.android.yamba;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import com.marakana.android.yamba.svc.YambaService;


public class TimelineActivity extends BaseActivity {
    private static final String TAG_DETAIL = "DETAIL_FRAG";

    private boolean usingFragments;

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent i, int code) {
        if (!usingFragments) { startActivity(i); }
        else {
            launchDetailFragment(i.getStringExtra(TimelineDetailFragment.PARAM_TEXT));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        usingFragments = null != findViewById(R.id.timeline_detail);

        if (usingFragments) { addDetailFragment(); }
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

    private void addDetailFragment() {
        FragmentManager mgr = getFragmentManager();

        if (null != mgr.findFragmentByTag(TAG_DETAIL)) { return; }

        FragmentTransaction xact = mgr.beginTransaction();
        xact.add(
                R.id.timeline_detail,
                new TimelineDetailFragment(),
                TAG_DETAIL);
        xact.commit();
    }


    private void launchDetailFragment(String detail) {
        FragmentTransaction xact = getFragmentManager().beginTransaction();
        xact.replace(
                R.id.timeline_detail,
                TimelineDetailFragment.newInstance(detail),
                TAG_DETAIL);
        xact.addToBackStack(null);
        xact.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        xact.commit();
    }
}
