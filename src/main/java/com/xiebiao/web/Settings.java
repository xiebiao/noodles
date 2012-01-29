package com.xiebiao.web;

import javax.servlet.ServletContext;

/**
 * 
 * @author xiaog (joyrap@gmail.com)
 * 
 */
public interface Settings {
	ServletContext getServletContext();

	String getInitParameter(String name);
}
