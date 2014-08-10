package com.demo.inject3;

import java.lang.reflect.Field;

public final class ActivityThread {
	private static final String CLASS_NAME = "android.app.ActivityThread";
	private static Field sInitialApplication_field;
	private static Field sSystemContext_field;
	
	/**
	 * get Application object form ActivityThread object.
	 * 
	 * @param object
	 *            ActivityThread object
	 * @return
	 */
	public static Object getInitialApplication(Object object) throws Exception {
		Object app = null;

		if (object != null && CLASS_NAME.equals(object.getClass().getName())) {

			if (sInitialApplication_field == null) {
				Class<?> ActivityThread_class = object.getClass();
				sInitialApplication_field = ActivityThread_class.getDeclaredField("mInitialApplication");
				sInitialApplication_field.setAccessible(true);
			}

			if (sInitialApplication_field != null) {
				app = sInitialApplication_field.get(object);
			}
		}

		return app;
	}
	
	public static Object getSystemContext() throws Exception {
		Object context = null;

		if (sSystemContext_field == null) {
			Class<?> ActivityThread_class = Class.forName("android.app.ActivityThread");
			sSystemContext_field = ActivityThread_class.getDeclaredField("mSystemContext");
			sSystemContext_field.setAccessible(true);
		}

		context = sSystemContext_field.get(null);

		return context;
	}

}
