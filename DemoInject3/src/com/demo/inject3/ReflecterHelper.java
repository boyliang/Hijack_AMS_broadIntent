package com.demo.inject3;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ����ֶη�����ȡ�����࣬Ϊ�����Ч�ʣ�������״̬���Ĺ�����ʽ
 * @author boyliang
 */
public final class ReflecterHelper {
	public static Class<?> sCurrentClass;
	
	/**
	 * ����
	 * @param name �������·��
	 * @return �Ƿ����óɹ�
	 */
	public final static boolean setClass(String name){
		Class<?> tmpClass = null;
		try {
			tmpClass = Class.forName(name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ExceptionInInitializerError e) {
			e.printStackTrace();
		} catch (LinkageError e) {
			e.printStackTrace();
		}
		
		sCurrentClass = tmpClass;
		return tmpClass != null;
	}
	
	public final static int getStaticIntValue(String name, int defvalue){
		int result = defvalue; 
		Field field = getField(name);
		
		if(field != null){
			try {
				result = field.getInt(null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public final static int getIntValue(Object owner, String name, int defvalue){
		int result = defvalue; 
		setClass(owner.getClass().getName());
		Field field = getField(name);
		
		if(field != null){
			try {
				result = field.getInt(owner);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	private final static Field getField(String name){
		Field field = null;
		try {
			field = sCurrentClass.getDeclaredField(name);
			field.setAccessible(true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
		}
		
		return field;
	}
	
	/**
	 * �õ�ĳ����ľ�̬����
	 * 
	 * @param className
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static Object getStaticValue(String fieldName){
		Field field = getField(fieldName);
		if (field==null) {
			return null;
		}
		Object result = null;
		
		try {
			result = field.get(null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/**
     * ʵ��������
     * @param className
     * @param args
     * @return
     * @throws Exception
     */
	static public Object newInstance(String className, Object... args) throws Exception {
		Class<?> newoneClass = Class.forName(className);
		Class<?>[] argsClass = null;

		if (args != null && args.length > 0) {
			argsClass = new Class<?>[args.length];

			for (int i = 0, j = args.length; i < j; i++) {
				argsClass[i] = args[i].getClass();
				if (argsClass[i] == Integer.class) {
					argsClass[i] = int.class;
				} else if (argsClass[i] == Boolean.class) {
					argsClass[i] = boolean.class;
				}
			}
		}
        
        Constructor<?> cons = newoneClass.getConstructor(argsClass);
        
        return cons.newInstance(args);
    }
	
	/**
     * ִ��ĳ����ķ���
     * 
     * @param owner
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     */
	static public Object invokeMethod(Object owner, String methodName, Object... args) throws Exception {
        Class<?> ownerClass = owner.getClass();
        Class<?>[] argsClass = null;
        
		if (args != null && args.length > 0) {
			argsClass = new Class<?>[args.length];
			
			for (int i = 0, j = args.length; i < j; i++) {
				argsClass[i] = args[i].getClass();
				if (argsClass[i] == Integer.class) {
					argsClass[i] = int.class;
				} else if (argsClass[i] == Boolean.class) {
					argsClass[i] = boolean.class;
				}
			}
		}
        
        Method method = ownerClass.getDeclaredMethod(methodName, argsClass);
        method.setAccessible(true);
        return method.invoke(owner, args);
    }
	
	static public Object invokeStaticMethod(Class<?> claxx, String methodName, Object... args) throws Exception {
        Class<?>[] argsClass = null;
        
		if (args != null && args.length > 0) {
			argsClass = new Class<?>[args.length];
			
			for (int i = 0, j = args.length; i < j; i++) {
				argsClass[i] = args[i].getClass();
				if (argsClass[i] == Integer.class) {
					argsClass[i] = int.class;
				} else if (argsClass[i] == Boolean.class) {
					argsClass[i] = boolean.class;
				}
			}
		}
        
        Method method = claxx.getDeclaredMethod(methodName, argsClass);
        method.setAccessible(true);
        return method.invoke(null, args);
    }
	
	/**
     * ��ȡĳ���������
     * 
     * @param owner
     * @param fieldName
     * @return
     * @throws Exception
     */
	static public Object getProperty(Object owner, String fieldName) throws Exception {
        Class<?> ownerClass = owner.getClass();
        
        Field field = ownerClass.getField(fieldName);
        
        Object property = field.get(owner);

        return property;
    }

}
