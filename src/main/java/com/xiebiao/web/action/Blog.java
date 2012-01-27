package com.xiebiao.web.action;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.xiebiao.web.annotation.Mapping;
import com.xiebiao.web.renderer.Renderer;
import com.xiebiao.web.renderer.TemplateRenderer;

public class Blog {
	private String name;
	private int id;
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Mapping("/blog/${id}")
	public Renderer blog() {
		LOG.debug("Renderer /blog/show");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", this.getName());
		map.put("id", this.getId());
		return new TemplateRenderer("Blog.html", map);
	}

	@Mapping("/blog/show")
	public Renderer show2() {
		LOG.debug("Renderer /blog/show");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", this.getName());
		map.put("id", this.getId());
		return new TemplateRenderer("Blog.html", map);
	}
}
