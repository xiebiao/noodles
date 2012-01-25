package com.xiebiao.web.action;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.xiebiao.web.annotation.Mapping;
import com.xiebiao.web.renderer.Renderer;
import com.xiebiao.web.renderer.TemplateRenderer;

public class Home implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private org.slf4j.Logger LOG = LoggerFactory.getLogger(Home.class);

	public Object excute() {
		LOG.debug("excute");
		return null;
	}

	@Mapping("/home")
	public Renderer home() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xiaog", "man");
		return new TemplateRenderer("Home.html", map);
	}
}
