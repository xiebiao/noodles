package com.xiebiao.web.renderer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiebiao.web.exception.RenderException;

public interface Renderer {
	public void render(ServletContext context, HttpServletRequest request,
			HttpServletResponse response) throws RenderException;
}
