package com.example.yamba;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity {

	private TextView status;
	private Button zap;
	private int state = 0;
	private EditText myName;
    private CheckBox flyKite;
    private TextView header;
	
	private void log(String s) {
      //if (BuildConfig.DEBUG) {
	    Log.i("tag", s);
      //}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	log("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zap = (Button) findViewById(R.id.zap_button);
    	status = (TextView) findViewById(R.id.living_status);
    	myName = (EditText) findViewById(R.id.status);
    	flyKite = (CheckBox) findViewById(R.id.checkBox1);
    	header = (TextView) findViewById(R.id.header);
    	
    	zap.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.v("FROM_DISPATCH_TOUCH_EVENT", "Action = " + event.getAction());

				int action = event.getAction();
				
				if (action == MotionEvent.ACTION_UP) {
					header.setText("Thunderstorm Simulator 2000!");
				} else {
					header.setText("ZZZZZZZZZZZZZZZZZZZZZZZZZAPPP!");
					
				}
				
				if (action == MotionEvent.ACTION_UP && flyKite.isChecked()) {
				if (state % 2 == 0) {
		    	  status.setText("You are currently: dead");
		    	  status.setTextColor(Color.RED);
		    	  state = 1;
		    	  myName.setText("Ben Franklin's Corpse");
				} else if (state % 2 == 1) {
		  		  status.setText("You are currently: undead");
			   	  status.setTextColor(Color.RED);
			   	  state = 2;
		    	  myName.setText("Electrified Zombie Ben Franklin");
				} 
				}
				return false;
			}
    	});

    	
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	protected void onDestroy() {
    	log("onDestroy");
		super.onDestroy();
	}


	@Override
	protected void onPause() {
    	log("onPause");
		super.onPause();
	}


	@Override
	protected void onResume() {
    	log("onResume");
		super.onResume();
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
    	log("onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}


	@Override
	protected void onStop() {
    	log("onStop");
		super.onStop();
	}
	
	
	
    
}
