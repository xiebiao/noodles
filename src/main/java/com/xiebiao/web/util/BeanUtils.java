package com.xiebiao.web.util;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.xiebiao.web.converter.ConverterHelper;

public final class BeanUtils {
	private static final org.slf4j.Logger LOG = LoggerFactory
			.getLogger(BeanUtils.class);

	public static Method getMethod(Object bean, String methodName)
			throws Exception {
		Method[] methods = bean.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				return bean.getClass().getMethod(methodName,
						method.getParameterTypes());
			}
		}
		return null;
	}

	public static void setProperty(Object bean, String name, String value)
			throws Exception {
		String methodName = "set" + StringUtils.firstLetterUpperCase(name);
		Method method = getMethod(bean, methodName);
		Object convertValue = null;
		try {
			if (method != null) {
				Class type = method.getParameterTypes()[0];
				if (type.equals(value.getClass())) {
					method.invoke(bean, value);
					return;
				}
				if (type.equals(Integer.class)) {
					convertValue = ConverterHelper.toInteger((String) value);
					method.invoke(bean, convertValue);
				} else if (type.getName().equals("int")) {
					convertValue = ConverterHelper.toInteger(value).intValue();
					method.invoke(bean, convertValue);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setProperties(Object bean,
			Map<String, String> propertyValue) throws Exception {
		Iterator it = propertyValue.keySet().iterator();
		while (it.hasNext()) {
			String name = (String) it.next();
			String value = (String)propertyValue.get(name);
			if (value == null)
				continue;
			setProperty(bean, name, value);
		}
	}
}
