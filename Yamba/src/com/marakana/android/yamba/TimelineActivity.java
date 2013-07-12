package com.marakana.android.yamba;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;

import com.marakana.android.yamba.svc.YambaService;


public class TimelineActivity extends BaseActivity {
	private static final String TAG_DETAIL = "DETAIL_FRAG";
	private static boolean usingFragments = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        
        usingFragments = null != findViewById(R.id.timeline_detail);
        
        if (usingFragments) { addDetailFragment(); }
    }

    @Override
	public void startActivityFromFragment(Fragment fra, Intent i,
			int req, Bundle opts) {
    	if (!usingFragments) { startActivity(i); }
    	else {
    		launchDetailFragment(i.getStringExtra(TimelineDetailFragment.PARAM_TEXT));
    	}
//    	super.startActivityFromFragment(fra, i, req, opts);
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
    
    private void launchDetailFragment(String status) {
    	FragmentTransaction xact = getFragmentManager().beginTransaction();
    	xact.add(
    			R.id.timeline_detail,
    			TimelineDetailFragment.newInstace(status),
    			TAG_DETAIL);
    	xact.addToBackStack(null);
    	xact.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    	xact.commit();
    }
}
