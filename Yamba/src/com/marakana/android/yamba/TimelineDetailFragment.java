/* $Id: $
   Copyright 2013, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.marakana.android.yamba;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class TimelineDetailFragment extends Fragment {
    public static final String PARAM_TEXT = "TimelineDetailFragment.DETAIL";

    public static TimelineDetailFragment newInstance(String detail) {
        TimelineDetailFragment frag = new TimelineDetailFragment();
        if (null != detail) {
            Bundle b = new Bundle();
            b.putString(PARAM_TEXT, detail);
            frag.setArguments(b);
        }

        return frag;
    }


    private TextView contents;
    private String details;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        if (null == state) { state = getArguments(); }
        setContent(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        contents = (TextView) inflater.inflate(R.layout.fragment_timeline_detail, parent, false);
        setContent(state);
        return contents;
    }

    public void setContent(Bundle state) {
        if (null != state) { details = state.getString(PARAM_TEXT); }
        if (null != contents) { contents.setText(details); }
     }
}
