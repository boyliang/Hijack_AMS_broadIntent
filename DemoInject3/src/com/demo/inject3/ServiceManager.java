package com.demo.inject3;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.os.IBinder;

/**
* ServiceManager��װ
* @author boyliang
*/

@SuppressWarnings("unchecked")
public final class ServiceManager {
	private static Class<?> mServiceManagerClass;
	
	private static Method mGetServiceMethod;
	private static Method mAddServiceMethod;
	private static Method mCheckServiceMethod;
	private static Method mListServiceMethod;
	private static HashMap<String, IBinder> sCache;
	
	static{
		try {
			mServiceManagerClass = Class.forName("android.os.ServiceManager");
			mGetServiceMethod = mServiceManagerClass.getDeclaredMethod("getService", String.class);
			mAddServiceMethod = mServiceManagerClass.getDeclaredMethod("addService", String.class, IBinder.class);
			mCheckServiceMethod = mServiceManagerClass.getDeclaredMethod("checkService", String.class);
			mListServiceMethod = mServiceManagerClass.getDeclaredMethod("listServices", new Class[]{});
			
			
			Field sCacheField = mServiceManagerClass.getDeclaredField("sCache");
			sCacheField.setAccessible(true);
			sCache = (HashMap<String, IBinder>) sCacheField.get(null);
			
		}  catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private ServiceManager(){}
	
	private static Object invoke(Method method, Object... objs){
		Object result = null;
		try {
			result = method.invoke(null, objs);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public static IBinder getService(String name){
		return (IBinder) invoke(mGetServiceMethod, name);
	}
	
	/**
	 * @param name
	 * @param binder
	 */
	public static void addService(String name, IBinder binder){
		invoke(mAddServiceMethod, name, binder);
	}
	
	/**
	 * @param name
	 * @return
	 */
	public static IBinder checkService(String name){
		return (IBinder) invoke(mCheckServiceMethod, name);
	}
	
	/**
	 * @return
	 */
	public static String[] listService(){
		return (String[]) invoke(mListServiceMethod, new Object[0]);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public static synchronized void setServiceToCache(String key, IBinder value){
		sCache.put(key, value);
	}
	
	/**
	 * @param key
	 * @return
	 */
	public static synchronized IBinder getServiceFromCache(String key){
		return sCache.get(key);
	}
	
	/**
	 * @param key
	 * @return
	 */
	public static synchronized IBinder getServiceBinderByNative(String key){
		IBinder binder;
		HashMap<String, IBinder> tmpMap = new HashMap<String, IBinder>(sCache);
		sCache.clear();
		binder = getService(key);
		sCache.putAll(tmpMap);
		return binder;
	}
}