package com.annmars.iloveppp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

abstract class BrowserActivityBase extends Activity {

	protected static String TAG = "MARS";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

}
