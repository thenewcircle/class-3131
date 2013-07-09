package com.marakana.android.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class StatusActivity extends Activity {

  private final String LOG_TAG = "Blake";

  private int statusMax;
  private int statusWarn;
  private int statusErr;
  private int green;
  private int yellow;
  private int red;
  private TextView count;
  private TextView status;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (BuildConfig.DEBUG)
      Log.d(LOG_TAG, "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_status);

    statusMax = Integer.parseInt(getResources().getString(R.string.status_max));
    statusWarn = Integer.parseInt(getResources().getString(R.string.status_warn));
    statusErr = Integer.parseInt(getResources().getString(R.string.status_err));

    green = getResources().getColor(R.color.green);
    yellow = getResources().getColor(R.color.yellow);
    red = getResources().getColor(R.color.red);

    count = (TextView) findViewById(R.id.status_count);
    status = (TextView) findViewById(R.id.status_text);

    status.addTextChangedListener(new TextWatcher() {

      @Override
      public void afterTextChanged(Editable s) {
        updateCount();
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
   * Makes the count text view reflect the number of characters in the status text view, coloring it
   * with the following scheme:
   * - Green for greater than statusWarn
   * - Yellow for greater than statusErr
   * - Red otherwise
   */
  private void updateCount() {
    int newCount = statusMax - status.getText().length();
    count.setText(String.valueOf(newCount));
    count.setTextColor(newCount > statusWarn ? green : newCount > statusErr ? yellow : red);
  }

  @Override
  protected void onStart() {
    if (BuildConfig.DEBUG)
      Log.d(LOG_TAG, "onStart");
    super.onStart();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    if (BuildConfig.DEBUG)
      Log.d(LOG_TAG, "onCreateOptionsMenu");
    getMenuInflater().inflate(R.menu.status, menu);
    return true;
  }

  @Override
  protected void onDestroy() {
    if (BuildConfig.DEBUG)
      Log.d(LOG_TAG, "onDestroy");
    super.onDestroy();
  }

  @Override
  protected void onPause() {
    if (BuildConfig.DEBUG)
      Log.d(LOG_TAG, "onPause");
    super.onPause();
  }

  @Override
  protected void onResume() {
    if (BuildConfig.DEBUG)
      Log.d(LOG_TAG, "onResume");
    super.onResume();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    Log.d(LOG_TAG, "onSaveInstanceState");
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onStop() {
    Log.d(LOG_TAG, "onStop");
    super.onStop();
  }

}
