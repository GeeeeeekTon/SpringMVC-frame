package com.leo.core.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 * 
 * @date 2013-10-25 下午01:56:28
 * @author leo
 * @version 1
 */

@SuppressWarnings("unchecked")
public class ReflectUtil {
	private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

	/**
	 * 通过反射设置该类属性值
	 * 
	 * @param target
	 * @param fname
	 * @param ftype
	 * @param fvalue
	 */
	public static void setFieldValue(Object target, String fname, Class ftype,
			Object fvalue) {
		if (target == null
				|| fname == null
				|| "".equals(fname)
				|| (fvalue != null && !ftype
						.isAssignableFrom(fvalue.getClass()))) {
			return;
		}
		Class clazz = target.getClass();
		try {
			Method method = clazz.getDeclaredMethod("set"
					+ Character.toUpperCase(fname.charAt(0))
					+ fname.substring(1), ftype);
			if (!Modifier.isPublic(method.getModifiers())) {
				method.setAccessible(true);
			}
			method.invoke(target, fvalue);

		} catch (Exception me) {
			logger.error(me.toString());
			try {
				Field field = clazz.getDeclaredField(fname);
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}
				field.set(target, fvalue);
			} catch (Exception fe) {
				logger.error(fe.toString());
			}
		}
	}
	
	/**
	 * 通过反射设置父类属性值
	 * 
	 * @param target
	 * @param fname
	 * @param ftype
	 * @param fvalue
	 */
	public static void setParentFieldValue(Object target, String fname, Class ftype,
			Object fvalue) {
		if (target == null
				|| fname == null
				|| "".equals(fname)
				|| (fvalue != null && !ftype
						.isAssignableFrom(fvalue.getClass()))) {
			return;
		}
		Class clazz = target.getClass().getSuperclass();
		try {
			Method method = clazz.getDeclaredMethod("set"
					+ Character.toUpperCase(fname.charAt(0))
					+ fname.substring(1), ftype);
			if (!Modifier.isPublic(method.getModifiers())) {
				method.setAccessible(true);
			}
			method.invoke(target, fvalue);

		} catch (Exception me) {
			logger.error(me.toString());
			try {
				Field field = clazz.getDeclaredField(fname);
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}
				field.set(target, fvalue);
			} catch (Exception fe) {
				logger.error(fe.toString());
			}
		}
	}
	
	/**
	 * 根据字段名称通过反射获得父类字段类
	 * @param target
	 * @param fieldName
	 * @return
	 */
	public static Field getParentFieldByFieldName(Object target, String fieldName){
		Field field = null;
		if (target == null || fieldName == null || "".equals(fieldName)) {
			return null;
		}
		Class parentClazz = target.getClass().getSuperclass();
		try {
			field = parentClazz.getDeclaredField(fieldName);
		} catch (Exception me) {
			logger.error(me.getMessage());
		}
		
		return field;
	}
	
	/**
	 * 根据字段名称通过反射获得字段类
	 * @param target
	 * @param fieldName
	 * @return
	 */
	public static Field getFieldByFieldName(Object target, String fieldName){
		Field field = null;
		if (target == null || fieldName == null || "".equals(fieldName)) {
			return null;
		}
		Class clazz = target.getClass();
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (Exception me) {
			logger.error(me.getMessage());
		}
		
		return field;
	}

	/**
	 * 通过反射获得该类的所有属性
	 * 
	 * @param target
	 * @return
	 */
	public static Field[] getAllFields(Object target) {
		Field[] fields = null;
		if (target == null) {
			return null;
		}
		
		try {
			// 获得该类中所有的属性
			Class clazz = target.getClass();
			fields = clazz.getDeclaredFields();
		} catch (Exception me) {
			logger.error(me.getMessage());
		}
		
		return fields;
	}
	
	/**
	 * 通过反射获得该类所有父类的属性
	 * @param target
	 * @return
	 */
	public static Field[] getAllParentFields(Object target) {
		Field[] fields = null;
		if (target == null) {
			return null;
		}
		
		try {
			// 获得该类父类中所有的属性
			Class superClazz = target.getClass().getSuperclass();
			fields = superClazz.getDeclaredFields();
			
		} catch (Exception me) {
			logger.error(me.getMessage());
		}
		
		return fields;
	}
	
	/**
	 * 通过反射获得该类属性值
	 * 
	 * @param target
	 * @param fname
	 * @return
	 */
	public static Object getFieldValue(Object target, String fname) {
		Object reslut = null;
		if (target == null || fname == null || "".equals(fname)) {
			return null;
		}
		Class clazz = target.getClass();
		try {
			Field field = clazz.getDeclaredField(fname);
			boolean isAccessible = field.isAccessible();
			
			field.setAccessible(true);
			reslut = field.get(target);
			
			field.setAccessible(isAccessible);

		} catch (Exception me) {
			logger.error(me.getMessage());
		}
		return reslut;
	}
	
	/**
	 * 通过反射获得父类属性值
	 * 
	 * @param target
	 * @param fname
	 * @return
	 */
	public static Object getParentFieldValue(Object target, String fname) {
		Object reslut = null;
		if (target == null || fname == null || "".equals(fname)) {
			return null;
		}
		Class parentClazz = target.getClass().getSuperclass();
		try {
			Field field = parentClazz.getDeclaredField(fname);
			boolean isAccessible = field.isAccessible();
			
			field.setAccessible(true);
			reslut = field.get(target);
			
			field.setAccessible(isAccessible);

		} catch (Exception me) {
			logger.error(me.getMessage());
		}
		return reslut;
	}
	
	/**
     * 获取obj对象fieldName的Field
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldNameNew(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值
     *
     * @param obj
     * @param fieldName
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getFieldByFieldNameNew(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     *
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getFieldByFieldNameNew(obj, fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }
}
