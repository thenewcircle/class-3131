package com.marakana.android.yamba;

import com.marakana.android.yamba.svc.YambaService;

import android.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class StatusFragment extends ListFragment {
    private static final String TAG = "STATUS";

    private TextView count;
    private EditText status;

    private int okColor;
    private int warnColor;
    private int errColor;

    private int statusLenMax;
    private int warnMax;
    private int errMax;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        Resources rez = getResources();
        okColor = rez.getColor(R.color.green);
        statusLenMax = rez.getInteger(R.integer.status_len_max);
        warnColor = rez.getColor(R.color.yellow);
        warnMax = rez.getInteger(R.integer.warn_max);
        errColor = rez.getColor(R.color.red);
        errMax = rez.getInteger(R.integer.err_max);

        View v = inflater.inflate(R.layout.fragment_status, parent, false);

        Button submit = (Button) v.findViewById(R.id.status_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { postStatus(); }
        });

        count = (TextView) v.findViewById(R.id.status_count);

        status = (EditText) v.findViewById(R.id.status_status);
        status.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { updateCount(); }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int n, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int n) { }
        });
        
        return v;
    }

    void postStatus() {
        String statusMsg = status.getText().toString();
        Log.d(TAG, "post: " + statusMsg);

        if (TextUtils.isEmpty(statusMsg)) { return; }

        status.setText("");

        YambaService.post(getActivity(), statusMsg);
    }

    void updateCount() {
        int n = statusLenMax - status.getText().length();
        int color;
        if (n > warnMax) { color = okColor; }
        else if (n > errMax) { color = warnColor; }
        else  { color = errColor; }

        count.setText(String.valueOf(n));
        count.setTextColor(color);
    }
}
