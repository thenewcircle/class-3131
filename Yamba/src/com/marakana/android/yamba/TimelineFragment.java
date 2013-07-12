package com.marakana.android.yamba;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class TimelineFragment extends ListFragment implements LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = 57;

    private static final String[] PROJ = {
        YambaContract.Timeline.Columns.ID,
        YambaContract.Timeline.Columns.CREATED_AT,
        YambaContract.Timeline.Columns.USER,
        YambaContract.Timeline.Columns.STATUS
    };

    private static final String[] FROM = new String[PROJ.length - 1];
    static { System.arraycopy(PROJ, 1, FROM, 0, FROM.length); }

    private static final int[] TO = new int[] {
        R.id.timeline_timestamp,
        R.id.timeline_user,
        R.id.timeline_status
    };

    static class TimelineBinder implements SimpleCursorAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View v, Cursor c, int col) {
            if (R.id.timeline_timestamp != v.getId()) { return false; }

            CharSequence s = "long ago";
            long t = c.getLong(col);
            if (0 < t) { s = DateUtils.getRelativeTimeSpanString(t); }
            ((TextView) v).setText(s);
            return true;
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(
                getActivity(),
                YambaContract.Timeline.URI,
                PROJ,
                null,
                null,
                YambaContract.Timeline.Columns.CREATED_AT + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int p, long id) {
        Cursor c = (Cursor) l.getItemAtPosition(p);

        Intent i = new Intent(getActivity(), TimelineDetailActivity.class);
        i.putExtra(
                TimelineDetailFragment.PARAM_TEXT,
                c.getString(c.getColumnIndex(YambaContract.Timeline.Columns.STATUS)));
        startActivity(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        View v = super.onCreateView(inflater, parent, state);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.timeline_row,
                null,
                FROM,
                TO,
                0);
        adapter.setViewBinder(new TimelineBinder());
        setListAdapter(adapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        return v;
    }
}
