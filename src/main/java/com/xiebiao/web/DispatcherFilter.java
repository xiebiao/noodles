package com.xiebiao.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

/**
 * 
 * @author xiaog (joyrap@gmail.com)
 * 
 */
public class DispatcherFilter implements Filter {
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());
	private RequestContext requestContext;

	public void init(final FilterConfig filterConfig) throws ServletException {
		requestContext = new RequestContext(new Setting() {
			public ServletContext getServletContext() {
				return filterConfig.getServletContext();
			}

			public String getInitParameter(String name) {
				return filterConfig.getInitParameter(name);
			}
		});
		requestContext.init();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		if (requestContext.service(req, res)) {
			return;
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Cann't find handle action for URL:"
						+ req.getRequestURL());
			}
			chain.doFilter(request, response);
		}
	}

	public void destroy() {
		this.requestContext.destroy();
	}

}
