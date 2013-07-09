
package com.marakana.android.yamba;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class StatusActivity extends Activity {
    private static final int STATUS_LEN = 140;
    private static final int WARN_LIMIT = 10;
    private static final int ERR_LIMIT = 0;


    private TextView count;
    private EditText status;

    private int okColor;
    private int warnColor;
    private int errColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
			getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
			? R.layout.activity_status : R.layout.activity_status_land
        );
        if (BuildConfig.DEBUG) { Log.d("###", "in onCreate"); }

        okColor = getResources().getColor(R.color.green);
        warnColor = getResources().getColor(R.color.yellow);
        errColor = getResources().getColor(R.color.red);

        count = (TextView) findViewById(R.id.status_count);
        status = (EditText) findViewById(R.id.status_status);

        status.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { updateCount(); }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
    }

    void updateCount() {
        int n = STATUS_LEN - status.getText().length();
        int color;
        if (n > WARN_LIMIT) { color = okColor; }
        else if (n > ERR_LIMIT) { color = warnColor; }
        else  { color = errColor; }

        count.setText(String.valueOf(n));
        count.setTextColor(color);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.status, menu);
        return true;
    }
}
