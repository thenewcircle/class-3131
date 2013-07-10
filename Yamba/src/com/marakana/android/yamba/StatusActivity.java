
package com.marakana.android.yamba;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.marakana.android.yamba.svc.YambaService;

public class StatusActivity extends Activity {
    private static final String TAG = "STATUS";

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

        okColor = getResources().getColor(R.color.green);
        warnColor = getResources().getColor(R.color.yellow);
        errColor = getResources().getColor(R.color.red);

        setContentView(R.layout.activity_status);

        Button submit = (Button) findViewById(R.id.status_submit);
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
            public void beforeTextChanged(CharSequence s, int start, int n, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int n) { }
        });
    }

    void postStatus() {
        String statusMsg = status.getText().toString();
        if (TextUtils.isEmpty(statusMsg)) { return; }

        status.setText("");

        YambaService.post(this, statusMsg);
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
