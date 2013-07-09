
package com.marakana.android.yamba;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class StatusActivity extends Activity {
    private static final String TAG = "STATUS";

    private static final int STATUS_LEN = 140;
    private static final int WARN_LIMIT = 10;
    private static final int ERR_LIMIT = 0;


    class Poster extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... statusMsg) {
            int msg = R.string.post_failed;
            try {
                yamba.postStatus(statusMsg[0]);
                msg = R.string.post_succeeded;
            }
            catch (YambaClientException e) {
                Log.e(TAG, "Post failed: " + e, e);
            }

            return Integer.valueOf(msg);
        }

        @Override
        protected void onPostExecute(Integer result) {
            postComplete(result.intValue());
        }
    }


     YambaClient yamba;

    private Poster poster;

    private TextView count;
    private EditText status;
    private Button submit;

    private int okColor;
    private int warnColor;
    private int errColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        if (BuildConfig.DEBUG) { Log.d("###", "in onCreate"); }

        okColor = getResources().getColor(R.color.green);
        warnColor = getResources().getColor(R.color.yellow);
        errColor = getResources().getColor(R.color.red);

        yamba = new YambaClient("student", "password", "http://yamba.marakana.com/api");

        submit = (Button) findViewById(R.id.button1);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { postStatus(); }
        });

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

    public void postComplete(int msg) {
        Toast.makeText(StatusActivity.this, msg, Toast.LENGTH_LONG).show();
        poster = null;
        submit.setEnabled(true);
    }

    void postStatus() {
        String statusMsg = status.getText().toString();
        if (TextUtils.isEmpty(statusMsg)) { return; }

        if (null != poster) { return; }
        poster = new Poster();
        submit.setEnabled(false);

        poster.execute(statusMsg);
    }

    void updateCount() {
        int n = STATUS_LEN - status.getText().length();
        int color;
        if (n > WARN_LIMIT) { color = okColor; }
        else if (n > ERR_LIMIT) { color = warnColor; }
        else  { color = errColor; }

        count.setText(String.valueOf(n));
        count.setTextColor(color + 0x0333333);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.status, menu);
        return true;
    }
}
