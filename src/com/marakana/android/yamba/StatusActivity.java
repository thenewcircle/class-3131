package com.marakana.android.yamba;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.marakana.android.yamba.svc.YambaService;

public class StatusActivity extends Activity {

  private final String LOG_TAG = StatusActivity.class.getSimpleName();

  private int statusMax;
  private int statusWarn;
  private int statusErr;

  private int green;
  private int yellow;
  private int red;

  private TextView count;
  private TextView status;
  private Button submit;

  private Resources res;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (BuildConfig.DEBUG)
      Log.d(LOG_TAG, "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_status);

    statusMax = getIntResource(R.string.status_max);
    statusWarn = getIntResource(R.string.status_warn);
    statusErr = getIntResource(R.string.status_err);

    green = getColorResource(R.color.green);
    yellow = getColorResource(R.color.yellow);
    red = getColorResource(R.color.red);

    count = (TextView) findViewById(R.id.status_count);
    status = (TextView) findViewById(R.id.status_text);
    submit = (Button) findViewById(R.id.submit);

    updateSubmit();
    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
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
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }
    });
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
    YambaService.post(this, status.getText().toString());
    status.setText("");
  }

  private int getColorResource(int colorId) {
    return getRes().getColor(colorId);
  }

  private int getIntResource(int stringId) {
    return Integer.parseInt(getRes().getString(stringId));
  }

  private Resources getRes() {
    if (res == null)
      res = getResources();
    return res;
  }

}
