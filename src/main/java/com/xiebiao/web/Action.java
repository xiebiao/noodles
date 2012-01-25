package com.xiebiao.web;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

public class Action {
	private Object instance;
	private Method method;
	private Map<String,Object> arguments = new HashMap<String,Object>();
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());

	public Action(Object instance, Method method) {
		this.instance = instance;
		this.method = method;		
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

}
