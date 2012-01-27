package com.xiebiao.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

import com.xiebiao.web.exception.ExecuteException;
import com.xiebiao.web.util.BeanUtils;

public class ActionExecutor {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Action action;
	private Map<String, String> arguments;
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());

	public ActionExecutor(HttpServletRequest request,
			HttpServletResponse response, Action action,
			Map<String, String> arguments) {
		this.request = request;
		this.response = response;
		this.action = action;
		this.arguments = arguments;

	}

	/**
	 * 执行Action
	 * 
	 * @return
	 */
	public Object excute() throws ExecuteException {
		try {
			BeanUtils.setProperties(action.getInstance(), arguments);
			return action.getMethod().invoke(action.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExecuteException(e.getMessage());
		}

	}
}
