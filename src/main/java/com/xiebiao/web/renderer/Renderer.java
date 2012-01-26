package com.xiebiao.web.renderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiebiao.web.ActionContext;
import com.xiebiao.web.exception.RenderException;

public interface Renderer {
	public void render(ActionContext actionContext, HttpServletRequest request,
			HttpServletResponse response) throws RenderException;
}
