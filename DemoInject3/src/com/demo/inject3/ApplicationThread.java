package com.demo.inject3;

import java.lang.reflect.Field;

public class ApplicationThread {
	private static final String CLASS_NAME = "android.app.ActivityThread$ApplicationThread";
	private static Field sThis$0_field;
	
	/**
	 * get ActivityThread object from ApplicationThread object.
	 * @param obj ApplicationThread object
	 * @return
	 */
	public static Object getActivityThreadObj(Object obj) throws Exception{
		Object result = null;
		
		if (obj != null && CLASS_NAME.equals(obj.getClass().getName())) {

			if (sThis$0_field == null) {
				Class<?> ApplicatioinThread_class = obj.getClass();
				sThis$0_field = ApplicatioinThread_class.getDeclaredField("this$0");
				sThis$0_field.setAccessible(true);
			}

			if (sThis$0_field != null) {
				result = sThis$0_field.get(obj);
			}
		}
		
		return result;
	}
}
