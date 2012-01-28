package com.xiebiao.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

import com.xiebiao.web.annotation.Mapping;
import com.xiebiao.web.exception.MappingException;
import com.xiebiao.web.renderer.Renderer;

/**
 * 
 * @author xiaog
 * 
 */
public class RequestContext {
	private static final ThreadLocal<RequestContext> contextThreadLocal = new ThreadLocal<RequestContext>();
	public static String ENCODING = "UTF-8";
	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static Map<UrlMapper, Action> urlMapperMap = new HashMap<UrlMapper, Action>();
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());
	private static UrlMapper[] urlMapperArray;
	private String packages;

	public RequestContext(Setting setting) {
		this.servletContext = setting.getServletContext();
		this.packages = setting.getInitParameter("packages");
		if (packages == null) {
			packages = "com.xiebiao.web.action";
		}
	}

	public static void set(RequestContext requestContext) {
		contextThreadLocal.set(requestContext);
	}

	public static void remove() {
		contextThreadLocal.remove();
	}

	public static RequestContext getCurrent() {
		return contextThreadLocal.get();
	}

	public static Map<UrlMapper, Action> getUrlMapperMap() {
		return urlMapperMap;
	}

	public void init() {
		_loadActions();

	}

	private void _loadActions() {
		//
		File actionClassFilePath = new File(this.getClass().getClassLoader()
				.getResource("").getFile()
				+ File.separator + packages.replace(".", File.separator));
		String[] actionClassFiles = actionClassFilePath.list();
		if (LOG.isDebugEnabled()) {
			LOG.debug(actionClassFilePath.getAbsolutePath());
		}
		for (String _class : actionClassFiles) {
			String className = _class.replace(".class", "");
			try {
				Object actionObj = Class.forName(packages + "." + className)
						.newInstance();
				Method[] ms = actionObj.getClass().getMethods();
				for (Method m : ms) {
					if (_hasAnnotation(m)) {
						Mapping mapping = m.getAnnotation(Mapping.class);
						String url = mapping.value();
						Action action = new Action(actionObj, m,
								m.getParameterTypes());
						UrlMapper urlMapper = new UrlMapper(url);
						if (urlMapperMap.get(urlMapper) != null) {
							throw new MappingException(
									"'"
											+ url
											+ "'"
											+ " is matched more than one action method.");
						} else {
							urlMapperMap.put(urlMapper, action);
						}

					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		urlMapperArray = new UrlMapper[urlMapperMap.keySet().size()];
		Iterator<?> it = urlMapperMap.keySet().iterator();
		int sum = 0;
		while (it.hasNext()) {
			UrlMapper urlMapper = (UrlMapper) it.next();
			urlMapperArray[sum] = urlMapper;
			sum++;
		}
		// url sort
		Arrays.sort(urlMapperArray, new Comparator<UrlMapper>() {
			public int compare(UrlMapper o1, UrlMapper o2) {
				String url1 = o1.getUrl();
				String url2 = o2.getUrl();
				int $1 = url1.indexOf("${");
				int $2 = url2.indexOf("${");
				// url contains "${" tag
				if ($1 > $2) {
					return $1;
				}
				int c = url1.compareTo(url2);
				return c;
			}
		});
		if (LOG.isDebugEnabled()) {
			for (UrlMapper urlMapper : urlMapperArray) {
				LOG.debug(urlMapper.getUrl());
			}
		}
	}

	/**
	 * Check action's annotations
	 * 
	 * @param method
	 * @return
	 */
	private boolean _hasAnnotation(Method method) {
		Mapping mapping = method.getAnnotation(Mapping.class);
		if (mapping != null) {
			if (mapping.value() == null || mapping.value().equals("")) {
				LOG.error(method.toGenericString() + ":"
						+ "has error mapping value");
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	private void handleException() {

	}

	private void handleResult(ActionExecutor executor) {
		try {
			Object result = executor.excute();
			if (result instanceof Renderer) {
				Renderer renderer = (Renderer) result;
				renderer.render(getCurrent().getRequest(), getCurrent()
						.getResponse());

			} else if (result instanceof Redirector) {
				Redirector redirector = (Redirector) result;
				redirector.redirect();
			} else if (result instanceof Forward) {
				Forward forward = (Forward) result;
				forward.forward(forward);
			}
		} catch (Exception e) {
			e.printStackTrace();
			handleException();
		}
	}

	/**
	 * handle service
	 * 
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		this.request = req;
		this.response = res;
		set(this);
		String path = this.request.getContextPath();
		String url = this.request.getRequestURI().substring(path.length());
		ActionExecutor executor = null;
		for (UrlMapper urlMapper : urlMapperArray) {
			Map<String, String> parameterMap = urlMapper.getParameterMap(url);
			if (parameterMap != null) {
				Action action = urlMapperMap.get(urlMapper);
				executor = new ActionExecutor(this.request, this.response,
						action, parameterMap);
				break;
			}
		}
		if (executor != null) {
			this.handleResult(executor);
			remove();
			return true;
		}
		remove();
		return false;
	}

	public static UrlMapper[] getUrlMapperArray() {
		return urlMapperArray;
	}

	public void destroy() {
		this.request = null;
		this.response = null;
		this.servletContext = null;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
}
