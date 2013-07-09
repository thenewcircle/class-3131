package com.marakana.android.yamba;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class StatusActivity extends Activity {

  private final String LOG_TAG = StatusActivity.class.getSimpleName();

  private YambaClient yamba;

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

  class Poster extends AsyncTask<String, Void, Integer> {

    @Override
    protected Integer doInBackground(String... statusMsg) {
      int msg = R.string.post_failed;
      try {
        yamba.postStatus(statusMsg[0]);
        msg = R.string.post_succeeded;
      } catch (YambaClientException e) {
        Log.e(LOG_TAG, "Post failed: ", e);
      }
      return msg;
    }

    @Override
    protected void onPostExecute(Integer result) {
      postComplete(result);
    }

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (BuildConfig.DEBUG)
      Log.d(LOG_TAG, "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_status);

    yamba = new YambaClient("student", "password", "http://yamba.marakana.com/api");

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
    status.setEnabled(false);
    submit.setEnabled(false);
    new Poster().execute(status.getText().toString());
  }

  void postComplete(Integer result) {
    status.setEnabled(true);
    submit.setEnabled(true);
    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    if (result == R.string.post_succeeded)
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
