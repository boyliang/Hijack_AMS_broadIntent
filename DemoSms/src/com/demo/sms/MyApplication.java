package com.demo.sms;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public final class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.i("TTT", getPackageName() + " starts");
		
		Intent i = new Intent(this, MyService.class);
		startService(i);
	}
}
