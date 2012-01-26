package com.xiebiao.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Redirector {
	private String location;

	public Redirector(String location) {
		this.location = location;
	}

	public void redirect(ActionContext actionContext,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.sendRedirect(location);
	}
}
