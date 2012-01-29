package com.xiebiao.web.template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiebiao.web.exception.RenderException;
/**
 * 
 * @author xiaog (joyrap@gmail.com)
 *
 */
public interface Template {
	public void render(HttpServletRequest request, HttpServletResponse response)
			throws RenderException;
}
