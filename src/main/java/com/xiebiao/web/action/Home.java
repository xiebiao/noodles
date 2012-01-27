package com.xiebiao.web.action;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.xiebiao.web.Forward;
import com.xiebiao.web.Redirector;
import com.xiebiao.web.annotation.Mapping;
import com.xiebiao.web.renderer.Renderer;
import com.xiebiao.web.renderer.TemplateRenderer;

public class Home implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());
	private String name;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object excute() {
		LOG.debug("excute");
		return null;
	}

	@Mapping("/home/forward")
	public Forward forward() {
		LOG.debug("forward to blog");
		return new Forward("/blog/1");
	}

	@Mapping("/home/go")
	public Redirector go() {
		return new Redirector("http://xiebiao.com");
	}

	@Mapping("/home/${name}/${id}")
	public Renderer home() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", this.getName());
		map.put("id", this.getId());
		return new TemplateRenderer("Home.html", map);
	}
}
