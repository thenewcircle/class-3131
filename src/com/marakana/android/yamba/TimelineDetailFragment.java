package com.marakana.android.yamba;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimelineDetailFragment extends Fragment {

  public static final String PARAM_STATUS = "TimelineDetailFragment.STATUS";
  private TextView contents;
  private String details;

  public static TimelineDetailFragment newInstance(String status) {
    TimelineDetailFragment frag = new TimelineDetailFragment();
    if (status != null) {
      Bundle b = new Bundle();
      b.putString(PARAM_STATUS, status);
      frag.setArguments(b);
    }
    return frag;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    contents = (TextView) inflater.inflate(R.layout.fragment_timeline_detail, parent, false);
    setContent(savedInstanceState);
    return contents;
  }

  public void setContent(Bundle state) {
    if (state != null) details = state.getString(PARAM_STATUS);
    if (contents != null) contents.setText(details);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) savedInstanceState = getArguments();
    setContent(savedInstanceState);
  }
}
