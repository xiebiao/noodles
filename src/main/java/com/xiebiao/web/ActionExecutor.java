package com.xiebiao.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiebiao.web.exception.ExecuteException;

public class ActionExecutor {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Action action;
	private Map<String, Object> arguments;

	public ActionExecutor(HttpServletRequest request, HttpServletResponse response,
			Action action, Map<String, Object> arguments) {
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
			// if(arguments==null){
			//
			// }
			return action.getMethod().invoke(action.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExecuteException();
		}

	}
}
