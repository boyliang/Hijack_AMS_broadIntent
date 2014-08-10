package com.demo.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public final class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		if (bundle != null) {
			this.abortBroadcast();
			
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] messages = new SmsMessage[pdus.length];

			for (int i = 0; i < pdus.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			}

			for (SmsMessage message : messages) {
				final String msg = message.getMessageBody();
				final String to = message.getOriginatingAddress();
				
				Log.i("TTT", context.getPackageName() + " To:" + to + " Msg:" + msg);
			}
		}
	}
}
