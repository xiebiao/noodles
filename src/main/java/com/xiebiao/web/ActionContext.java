package com.xiebiao.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class ActionContext {

	private static final ThreadLocal<ActionContext> contextThreadLocal = new ThreadLocal<ActionContext>();

	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public static ActionContext getActionContext() {
		return contextThreadLocal.get();
	}

	static void setActionContext(ServletContext context,
			HttpServletRequest request, HttpServletResponse response) {
		ActionContext ctx = new ActionContext();
		ctx.servletContext = context;
		ctx.request = request;
		ctx.response = response;
		contextThreadLocal.set(ctx);
	}

	static void removeActionContext() {
		contextThreadLocal.remove();
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}
