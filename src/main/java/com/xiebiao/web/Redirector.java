package com.xiebiao.web;

public class Redirector {
	private String location;

	public Redirector(String location) {
		this.location = location;
	}

	public void redirect() throws Exception {
		RequestContext.getCurrent().getResponse().sendRedirect(location);
	}
}
