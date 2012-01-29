package com.xiebiao.web.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiebiao.web.RequestContext;
import com.xiebiao.web.exception.RenderException;

import freemarker.template.Configuration;

public class FreemarkerTemplate implements Template {
	private String _templateFile;
	private final static String _configFile = "freemarker.properties";
	private Map<String, Object> _data;
	private final static Configuration _cfg = new Configuration();

	public FreemarkerTemplate(String templateFile, Map<String, Object> data) {
		this._templateFile = templateFile;
		this._data = data;
		Properties p = new Properties();
		File f = new File(RequestContext.getCurrent().getServletContext()
				.getRealPath("")
				+ "/WEB-INF/" + _configFile);
		try {
			p.load(new FileInputStream(f));
			_cfg.setSettings(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void render(HttpServletRequest request, HttpServletResponse response)
			throws RenderException {

		try {
			String templatesDirectory = RequestContext.getCurrent()
					.getServletContext().getRealPath("")
					+ "/WEB-INF/templates/";
			_cfg.setDirectoryForTemplateLoading(new File(templatesDirectory));
			freemarker.template.Template template = _cfg
					.getTemplate(this._templateFile);
			response.setContentType("text/html; charset="
					+ RequestContext.ENCODING);
			Writer out = response.getWriter();
			template.process(_data, out);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RenderException("");
		}

	}
}
