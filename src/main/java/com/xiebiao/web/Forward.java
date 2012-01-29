package com.xiebiao.web;

import java.util.Map;

import org.slf4j.LoggerFactory;

import com.xiebiao.web.renderer.Renderer;
/**
 * 
 * @author xiaog (joyrap@gmail.com)
 *
 */
public class Forward {
	private String url;
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());

	public Forward(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof Forward) {
			Forward forwarder = (Forward) obj;
			if (forwarder.getUrl().equals(this.url)) {
				return true;
			}
		}
		return false;
	}

	private void forwardNext(Forward forwarder) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Forward to URL:" + forwarder.getUrl());
		}
		for (UrlMapper urlMapper : RequestContext.getCurrent()
				.getUrlMapperArray()) {
			Map<String, String> parameterMap = urlMapper
					.getParameterMap(forwarder.getUrl());
			if (parameterMap != null) {
				Action action = RequestContext.getUrlMapperMap().get(urlMapper);
				ActionExecutor actionExecutor = new ActionExecutor(action,
						parameterMap);
				Object result = actionExecutor.excute();
				if (result instanceof Forward) {
					Forward _forwarder = (Forward) result;
					if (_forwarder.equals(this)) {
						return;
					}
					forwardNext(_forwarder);
				} else if (result instanceof Renderer) {
					Renderer renderer = (Renderer) result;
					renderer.render(RequestContext.getCurrent().getRequest(),
							RequestContext.getCurrent().getResponse());
					return;
				}
			}
		}
	}

	public void forward(Forward forwarder) throws Exception {
		forwardNext(forwarder);
	}
}
