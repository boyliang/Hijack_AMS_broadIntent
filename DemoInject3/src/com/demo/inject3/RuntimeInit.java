package com.demo.inject3;

import java.lang.reflect.Method;

public final class RuntimeInit {
	private static Method sGetApplicationObject_method;
	
	static{
		Class<?> RuntimeInit_class;
		try {
			RuntimeInit_class = Class.forName("com.android.internal.os.RuntimeInit");
			sGetApplicationObject_method = RuntimeInit_class.getDeclaredMethod("getApplicationObject");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final Object getApplicationObject() throws Exception{
		return sGetApplicationObject_method != null ? sGetApplicationObject_method.invoke(null) : null;
	}
}
