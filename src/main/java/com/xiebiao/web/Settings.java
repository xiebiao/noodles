package com.xiebiao.web;

import javax.servlet.ServletContext;

public interface Settings {
	ServletContext getServletContext();

	String getInitParameter(String name);
}
