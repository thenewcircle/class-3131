package com.marakana.android.yamba;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActionBar bar = getActionBar();
    bar.setHomeButtonEnabled(true);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        openActivity(TimelineActivity.class);
        break;
      case R.id.menu_status:
        openActivity(StatusActivity.class);
        break;
      case R.id.menu_about:
        Toast.makeText(this, "Made by @andres", Toast.LENGTH_LONG).show();
        break;
      case R.id.menu_prefs:
        Toast.makeText(this, "Coming Soon!", Toast.LENGTH_LONG).show();
        break;
      default:
        return super.onOptionsItemSelected(item);
    }
    return true;
  }

  private void openActivity(Class<?> klass) {
    Intent i = new Intent(this, klass);
    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    startActivity(i);
  }
}
