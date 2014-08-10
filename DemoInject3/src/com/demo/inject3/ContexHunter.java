package com.demo.inject3;

import android.app.Application;
import android.content.Context;

/**
 * Context��ȡ
 * 
 * @author boyliang
 * 
 */
public final class ContexHunter {
	private static Context sContext = null;

	public static Context getContext() {

		if (sContext == null) {

			synchronized (ContexHunter.class) {
				if (sContext == null) {
					sContext = searchContextForSystemServer();
					if (sContext == null) {
						sContext = searchContextForZygoteProcess();
					}
				}
			}
		}

		return sContext;
	}

	private static Context searchContextForSystemServer() {
		Context result = null;

		try {
			result = (Context) ActivityThread.getSystemContext();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;

	}

	private static Context searchContextForZygoteProcess() {
		Context result = null;
		
		try {
			Object obj = RuntimeInit.getApplicationObject();
			if (obj != null) {
				obj = ApplicationThread.getActivityThreadObj(obj);
				if (obj != null) {
					result = (Application) ActivityThread.getInitialApplication(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
