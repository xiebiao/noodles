package com.xiebiao.web.renderer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiebiao.web.exception.RenderException;
import com.xiebiao.web.template.FreemarkerTemplate;
import com.xiebiao.web.template.Template;

public class TemplateRenderer implements Renderer {
	private String templatePath;
	private Map<String, Object> data;

	public TemplateRenderer(String templatePath) {
		this.templatePath = templatePath;
		this.data = new HashMap<String, Object>();
	}

	public TemplateRenderer(String templatePath, Map<String, Object> data) {
		this.templatePath = templatePath;
		this.data = data;
	}

	public void render(HttpServletRequest request, HttpServletResponse response)
			throws RenderException {
		Template template = new FreemarkerTemplate(this.templatePath, data);
		template.render(request, response);
	}

}
