package com.marakana.android.yamba;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class TimelineActivity extends ListActivity implements LoaderCallbacks<Cursor> {

  private static final int LOADER_ID = 45;
  private static final String[] PROJECTION = {
      YambaContract.Timeline.Columns.ID,
      YambaContract.Timeline.Columns.CREATED_AT,
      YambaContract.Timeline.Columns.USER,
      YambaContract.Timeline.Columns.STATUS
  };
  private static final String[] FROM = new String[PROJECTION.length - 1];
  static {
    System.arraycopy(PROJECTION, 1, FROM, 0, FROM.length);
  }

  private static final int[] TO = new int[] {
      R.id.status_timestamp,
      R.id.status_user,
      R.id.status_message
  };

  static class TimelineBinder implements SimpleCursorAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Cursor cursor, int column) {
      if (view.getId() != R.id.status_timestamp) return false;

      long t = cursor.getLong(column);
      CharSequence s = "long ago";
      if (t > 0) s = DateUtils.getRelativeTimeSpanString(t);

      ((TextView) view).setText(s);
      return true;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getListView().setBackgroundResource(R.drawable.background);

    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.timeline_row, null, FROM,
            TO, 0);
    adapter.setViewBinder(new TimelineBinder());
    setListAdapter(adapter);

    getLoaderManager().initLoader(LOADER_ID, null, this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.timeline, menu);
    return true;
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, YambaContract.Timeline.URI, PROJECTION, null, null,
            YambaContract.Timeline.Columns.CREATED_AT + " DESC");
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    ((SimpleCursorAdapter) getListAdapter()).swapCursor(cursor);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> cursor) {
    ((SimpleCursorAdapter) getListAdapter()).swapCursor(null);
  }

}
