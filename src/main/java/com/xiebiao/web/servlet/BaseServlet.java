package com.xiebiao.web.servlet;

import javax.servlet.http.HttpServlet;

import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;

public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Configuration cfg = new Configuration();
	private org.slf4j.Logger LOG = LoggerFactory.getLogger(BaseServlet.class);
}
