package com.demo.inject3;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * 短信广播优先级调整
 * @author boyliang
 */
final class SmsReceiverResorter {
	private static final String[] sActions = { "android.provider.Telephony.SMS_RECEIVED", "android.provider.Telephony.SMS_RECEIVED2", "android.provider.Telephony.GSM_SMS_RECEIVED" };
	private final String TAG = "SmsReceiverResorter";
	private HashMap<String, ArrayList<? extends IntentFilter>> mActionToFilter;
	private Field mPackageNameField;

	@SuppressWarnings("unchecked")
	public SmsReceiverResorter(IBinder am) {
		Class<?> claxx = am.getClass();
		try {
			Field field = claxx.getDeclaredField("mReceiverResolver");
			field.setAccessible(true);
			Object mReceiverResolver = field.get(am);

			claxx = mReceiverResolver.getClass();
			
			field = claxx.getSuperclass().getDeclaredField("mActionToFilter");
			field.setAccessible(true);

			mActionToFilter = (HashMap<String, ArrayList<? extends IntentFilter>>) field.get(mReceiverResolver);
			
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	/**
	 * 修改优先级
	 */
	public void updatePriority(String target_pkg) {
		
		if (mActionToFilter != null) {
			
			for (String action : sActions) {
				
				@SuppressWarnings("unchecked")
				ArrayList<IntentFilter> filters = (ArrayList<IntentFilter>) mActionToFilter.get(action);

				if (filters != null) {
					Log.i("TTT", "send sms broadcast");
					
					IntentFilter filter = null;

					for (IntentFilter f : filters) {
						String pkg = getPackageName(f);
						if (target_pkg.equals(pkg)) {
							filter = f;
							break;
						} 
					}

					// 调整顺序
					if (filter != null && filters.remove(filter) ) {
						
						filters.add(0, filter);
						filter = null;
						
						Log.i("TTT", target_pkg + " is the first now");
					}
				}
			}

		}
	}

	private String getPackageName(IntentFilter filter) {
		
		if (mPackageNameField == null && filter != null) {
			Class<?> claxx = filter.getClass();
			try {
				mPackageNameField = claxx.getDeclaredField("packageName");
				mPackageNameField.setAccessible(true);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
		
		String result = null;

		if (filter != null) {
			try {
				result = (String) mPackageNameField.get(filter);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
		
		return result;
	}
}
