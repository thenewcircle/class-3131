
package com.marakana.android.yamba;

import android.os.Bundle;

public class TimelineDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_timeline_detail);

        if (null == state) { state = getIntent().getExtras(); }
        if (null != state) {
            TimelineDetailFragment details
                = (TimelineDetailFragment)  getFragmentManager()
                    .findFragmentById(R.id.fragment_timeline_detail);
            details.setContent(state);
        }
    }
}
