package com.annmars.iloveppp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public abstract class BrowserActivity extends Activity {

	protected static String TAG = "MARS";

	public enum STATUS {
		LOADING, FAILURE, SUCCESS
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	abstract public void updateLoadingStatus(STATUS status);
}
