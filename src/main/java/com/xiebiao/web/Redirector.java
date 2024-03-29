package com.xiebiao.web;

/**
 * 
 * @author xiaog (joyrap@gmail.com)
 * 
 */
public class Redirector {
	private String location;

	public Redirector(String location) {
		this.location = location;
	}

	public void redirect() throws Exception {
		RequestHandler.getCurrent().getResponse().sendRedirect(location);
	}
}
