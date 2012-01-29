package com.xiebiao.web;

import java.util.Map;

import org.slf4j.LoggerFactory;

import com.xiebiao.web.exception.ExecuteException;
import com.xiebiao.web.util.BeanUtils;

public class ActionExecutor {
	private Action action;
	private Map<String, String> arguments;
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());

	public ActionExecutor(Action action, Map<String, String> arguments) {
		this.action = action;
		this.arguments = arguments;

	}

	/**
	 * excute action
	 * 
	 * @return
	 */
	public Object excute() throws ExecuteException {
		try {
			Object bean = Class.forName(
					action.getInstance().getClass().getName()).newInstance();
			BeanUtils.setProperties(bean, arguments);
			return action.getMethod().invoke(bean);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExecuteException(e.getMessage());
		}

	}
}
