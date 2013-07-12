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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.status, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_status:
				nextPage(StatusActivity.class);
				break;

			case android.R.id.home:
			case R.id.menu_timeline:
				nextPage(TimelineActivity.class);
				break;

			case R.id.menu_about:
				Toast.makeText(this, R.string.about, Toast.LENGTH_LONG).show();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}
		
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar aBar = getActionBar();
		aBar.setHomeButtonEnabled(true);
		aBar.setDisplayHomeAsUpEnabled(true);
	}

	private void nextPage(Class<?> klass) {
		Intent i = new Intent(this, klass);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);
	}

}
