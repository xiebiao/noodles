package com.xiebiao.web.renderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiebiao.web.exception.RenderException;

public interface Renderer {
	public void render(HttpServletRequest request, HttpServletResponse response)
			throws RenderException;
}
