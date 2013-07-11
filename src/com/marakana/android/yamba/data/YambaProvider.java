package com.marakana.android.yamba.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class YambaProvider extends ContentProvider {

  YambaDbHelper dbHelper;

  @Override
  public String getType(Uri arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean onCreate() {
    dbHelper = new YambaDbHelper(getContext());
    return null != dbHelper;
  }

  @Override
  public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) {

    return null;
  }

  @Override
  public Uri insert(Uri arg0, ContentValues arg1) {
    throw new UnsupportedOperationException("insert is not supported yet");
  }

  @Override
  public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
    throw new UnsupportedOperationException("update is not supported yet");
  }

  @Override
  public int delete(Uri arg0, String arg1, String[] arg2) {
    throw new UnsupportedOperationException("delete is not supported yet");
  }
}
