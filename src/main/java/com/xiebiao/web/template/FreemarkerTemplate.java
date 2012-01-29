package com.xiebiao.web.template;

import java.io.File;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

import com.xiebiao.web.RequestContext;
import com.xiebiao.web.exception.RenderException;

import freemarker.template.Configuration;

public class FreemarkerTemplate implements Template {
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());
	private String templateFile;
	private Map<String, Object> data;
	private final static Configuration cfg = new Configuration();
	
	public FreemarkerTemplate(String templateFile, Map<String, Object> data) {
		this.templateFile = templateFile;
		this.data = data;

	}

	public void render(HttpServletRequest request, HttpServletResponse response)
			throws RenderException {
		try {
			String templatesDirectory = RequestContext.getCurrent()
					.getServletContext().getRealPath("")
					+ "/WEB-INF/templates/";
			cfg.setDirectoryForTemplateLoading(new File(templatesDirectory));
			freemarker.template.Template template = cfg
					.getTemplate(this.templateFile);
			response.setContentType("text/html; charset="
					+ RequestContext.ENCODING);
			Writer out = response.getWriter();
			template.process(data, out);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RenderException(e.getMessage());
		}
	}
}
