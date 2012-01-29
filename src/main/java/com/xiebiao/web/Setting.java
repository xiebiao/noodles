package com.xiebiao.web;

import javax.servlet.ServletContext;

/**
 * 
 * @author xiaog (joyrap@gmail.com)
 * 
 */
public interface Setting {
	ServletContext getServletContext();

	String getInitParameter(String name);
}
