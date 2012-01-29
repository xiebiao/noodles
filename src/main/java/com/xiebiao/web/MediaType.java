package com.xiebiao.web;

/**
 * 
 * @author xiaog (joyrap@gmail.com)
 * 
 */
public final class MediaType {
	private String _name;
	public static final MediaType TEXT_PLAIN = new MediaType("text/plain");

	public MediaType(String name) {
		this._name = name;
	}

	public String getName() {
		return _name;
	}
}
