package com.xiebiao.web;

import java.lang.reflect.Method;

import org.slf4j.LoggerFactory;

public class Action {
	private Object instance;
	private Method method;
	private Class<?>[] argumentTypes;
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());

	public Action(Object instance, Method method, Class<?>[] argumentTypes) {
		this.instance = instance;
		this.method = method;
		this.argumentTypes = argumentTypes;
	}

	public Object getInstance() {
		return instance;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public Class<?>[] getArgumentTypes() {
		return argumentTypes;
	}

	public void setArgumentTypes(Class<?>[] argumentTypes) {
		this.argumentTypes = argumentTypes;
	}

}
