package com.marakana.android.yamba;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class TimelineFragment extends ListFragment implements LoaderCallbacks<Cursor> {

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
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    // Not needed since onCreateView does it for you in the case of ListFragment
    // View v = inflater.inflate(R.layout.activity_status, parent, false);

    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.timeline_row,
            null, FROM,
            TO, 0);
    adapter.setViewBinder(new TimelineBinder());
    setListAdapter(adapter);

    getLoaderManager().initLoader(LOADER_ID, null, this);

    return super.onCreateView(inflater, parent, savedInstanceState);
  }

  @Override
  public void onListItemClick(ListView l, View v, int p, long id) {
    Cursor c = (Cursor) l.getItemAtPosition(p);
    Intent i = new Intent(getActivity(), TimelineDetailActivity.class);
    i.putExtra(TimelineDetailFragment.PARAM_STATUS,
            c.getString(c.getColumnIndex(YambaContract.Timeline.Columns.STATUS)));
    startActivity(i);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(getActivity(), YambaContract.Timeline.URI, PROJECTION, null, null,
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
