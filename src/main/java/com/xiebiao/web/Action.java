package com.xiebiao.web;

import java.lang.reflect.Method;

import org.slf4j.LoggerFactory;

/**
 * 
 * @author xiaog (joyrap@gmail.com)
 * 
 */
public class Action {
	private Object _instance;
	private Method _method;
	private Class<?>[] _argumentTypes;
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());

	public Action(Object instance, Method method, Class<?>[] argumentTypes) {
		this._instance = instance;
		this._method = method;
		this._argumentTypes = argumentTypes;
	}

	public Object getInstance() {
		return _instance;
	}

	public Method getMethod() {
		return _method;
	}

	public void setMethod(Method method) {
		this._method = method;
	}

	public void setInstance(Object instance) {
		this._instance = instance;
	}

	public Class<?>[] getArgumentTypes() {
		return _argumentTypes;
	}

	public void setArgumentTypes(Class<?>[] argumentTypes) {
		this._argumentTypes = argumentTypes;
	}

}
