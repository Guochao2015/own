/**
 * 
 */
package org.own.commons.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Administrator
 * java内省机制
 */
public class BeanInfoUtil {
	
	/**
	 * 给bean 相应的属性设值
	 * @param t
	 * @param name
	 * @param value
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> void setBean(T t,String name,Object value) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		for(PropertyDescriptor propertydescriptor:getPropertyDescriptors(t)){
			if(propertydescriptor.getName().equals(name)){
				Method writeMethod = propertydescriptor.getWriteMethod();
				Class clazz = writeMethod.getParameterTypes()[0];
				if(clazz == Boolean.class){
					writeMethod.invoke(t, "true".equals(value));
				}else if(clazz == Integer.class){
					writeMethod.invoke(t, Integer.parseInt((String)value));
				}else if(clazz == java.lang.Double.class){
					writeMethod.invoke(t, Double.valueOf((String)value));
				}else if(clazz == java.lang.Float.class){
					writeMethod.invoke(t, Float.valueOf((String)value));
				}else if(clazz == java.lang.Long.class){
					writeMethod.invoke(t, Long.valueOf((String)value));
				}else{
					writeMethod.invoke(t, value);
				}
				
				break ; 
			}
		}
	}
	/**
	 * 获得bean 相应属性的值
	 * @param t
	 * @param name
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	@SuppressWarnings("unchecked")
	public static <T,V> V getBean(T t,String name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException{
		for(PropertyDescriptor propertydescriptor:getPropertyDescriptors(t)){
			if(propertydescriptor.getName().equals(name)){
				Method writeMethod = propertydescriptor.getReadMethod();
				return (V) writeMethod.invoke(t);
			}
		}
		return null;
	}
	private static <T> PropertyDescriptor[] getPropertyDescriptors(T t) throws IntrospectionException{
		PropertyDescriptor[] propertyDescriptors = CacheUtil.getInstance().getCache(t.getClass());
		if(propertyDescriptors != null){
			BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
			propertyDescriptors = beanInfo.getPropertyDescriptors();
		}
		return propertyDescriptors;
	}
}
