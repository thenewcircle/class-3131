package com.marakana.android.yamba;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.marakana.android.yamba.svc.YambaService;

public class StatusFragment extends Fragment {

  private final String LOG_TAG = StatusFragment.class.getSimpleName();

  private int statusMax;
  private int statusWarn;
  private int statusErr;

  private int green;
  private int yellow;
  private int red;

  private TextView count;
  private TextView status;
  private Button submit;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    if (BuildConfig.DEBUG)
      Log.d(LOG_TAG, "onCreate");

    Resources res = getResources();

    statusMax = res.getInteger(R.integer.status_max);
    statusWarn = res.getInteger(R.integer.warn_max);
    statusErr = res.getInteger(R.integer.err_max);

    green = res.getColor(R.color.green);
    yellow = res.getColor(R.color.yellow);
    red = res.getColor(R.color.red);

    View view = inflater.inflate(R.layout.fragment_status, parent, false);

    count = (TextView) view.findViewById(R.id.status_count);
    status = (TextView) view.findViewById(R.id.status_text);
    submit = (Button) view.findViewById(R.id.submit);

    updateSubmit();
    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("TEMP", "onClick");
        postStatus();
      }
    });

    status.addTextChangedListener(new TextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        updateCount();
        updateSubmit();
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {}

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    });

    return view;
  }

  /**
   * Makes the count text view show the number of possible characters remaining in the post,
   * coloring it with the following scheme:
   * - Green for greater than statusWarn
   * - Yellow for greater than statusErr
   * - Red otherwise
   */
  void updateCount() {
    int remaining = statusMax - status.getText().length();
    count.setText(String.valueOf(remaining));
    count.setTextColor(remaining > statusWarn ? green : remaining > statusErr ? yellow : red);
  }

  void updateSubmit() {
    submit.setEnabled(status.getText().length() > 0);
  }

  void postStatus() {
    YambaService.post(getActivity(), status.getText().toString());
    status.setText("");
  }

}
