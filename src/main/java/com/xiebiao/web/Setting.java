package com.xiebiao.web;

import javax.servlet.ServletContext;

public interface Setting {
	ServletContext getServletContext();

	String getInitParameter(String name);
}
