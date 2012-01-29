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
import com.xiebiao.web.exception.ExecuteException;
import com.xiebiao.web.exception.MappingException;
import com.xiebiao.web.exception.RenderException;
import com.xiebiao.web.renderer.Renderer;

/**
 * 
 * @author xiaog (joyrap@gmail.com)
 * 
 */
public class RequestHandler {
	private static final ThreadLocal<RequestHandler> contextThreadLocal = new ThreadLocal<RequestHandler>();
	public static String ENCODING = "UTF-8";
	private ServletContext _servletContext;
	private HttpServletRequest _request;
	private HttpServletResponse _response;
	private static Map<UrlMapper, Action> _urlMapperMap = new HashMap<UrlMapper, Action>();
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());
	private static UrlMapper[] _urlMapperArray;
	private String _packages;

	public RequestHandler(Setting settings) {
		this._servletContext = settings.getServletContext();
		this._packages = settings.getInitParameter("packages");
		if (_packages == null) {
			_packages = "com.xiebiao.web.action";
		}
	}

	public static void set(RequestHandler requestContext) {
		contextThreadLocal.set(requestContext);
	}

	public static void remove() {
		contextThreadLocal.remove();
	}

	public static RequestHandler getCurrent() {
		return contextThreadLocal.get();
	}

	public static Map<UrlMapper, Action> getUrlMapperMap() {
		return _urlMapperMap;
	}

	public void init() {
		_loadActions();

	}

	private void _loadActions() {
		//
		File actionClassFilePath = new File(this.getClass().getClassLoader()
				.getResource("").getFile()
				+ File.separator + _packages.replace(".", File.separator));
		String[] actionClassFiles = actionClassFilePath.list();
		if (LOG.isDebugEnabled()) {
			LOG.debug(actionClassFilePath.getAbsolutePath());
		}
		for (String _class : actionClassFiles) {
			String className = _class.replace(".class", "");
			try {
				Object actionObj = Class.forName(_packages + "." + className)
						.newInstance();
				Method[] ms = actionObj.getClass().getMethods();
				for (Method m : ms) {
					if (_hasAnnotation(m)) {
						Mapping mapping = m.getAnnotation(Mapping.class);
						String url = mapping.value();
						Action action = new Action(actionObj, m,
								m.getParameterTypes());
						UrlMapper urlMapper = new UrlMapper(url);
						if (_urlMapperMap.get(urlMapper) != null) {
							throw new MappingException(
									"'"
											+ url
											+ "'"
											+ " is matched more than one action method.");
						} else {
							_urlMapperMap.put(urlMapper, action);
						}

					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		_urlMapperArray = new UrlMapper[_urlMapperMap.keySet().size()];
		Iterator<?> it = _urlMapperMap.keySet().iterator();
		int sum = 0;
		while (it.hasNext()) {
			UrlMapper urlMapper = (UrlMapper) it.next();
			_urlMapperArray[sum] = urlMapper;
			sum++;
		}
		// url sort
		Arrays.sort(_urlMapperArray, new Comparator<UrlMapper>() {
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
			for (UrlMapper urlMapper : _urlMapperArray) {
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

	private void _handleException(Exception e) {
		try {
			if ((e instanceof RenderException)
					|| (e instanceof ExecuteException)) {
				// Return 500
				getCurrent().getResponse().sendError(500);
			}

		} catch (Exception e1) {
			e.printStackTrace();
		}
	}

	private void _handleResult(ActionExecutor executor) {
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
			_handleException(e);
		}
	}

	private Map<String, String> _parseQueryString(String queryString) {
		if (queryString == null || queryString.equals("")
				|| queryString.indexOf("=") == -1) {
			return null;
		}
		String[] nameValues = queryString.split("&");
		if (nameValues.length <= 1) {
			return null;
		}
		if (nameValues.length > 60) {
			LOG.warn("The queryString too long");
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < nameValues.length; i++) {
			String[] temp = nameValues[i].split("=");
			if (temp.length == 2 && !temp[0].equals("") && !temp[1].equals("")) {
				map.put(temp[0], temp[1]);
			}
		}
		return map;

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
		req.setCharacterEncoding(ENCODING);
		this._request = req;
		this._response = res;
		set(this);
		String path = getCurrent().getRequest().getContextPath();
		String uri = getCurrent().getRequest().getRequestURI()
				.substring(path.length());
		ActionExecutor executor = null;
		for (UrlMapper urlMapper : _urlMapperArray) {
			Map<String, String> parameterMap = urlMapper.getParameterMap(uri);
			if (parameterMap != null) {
				Map<String, String> queryStringParameterMap = this
						._parseQueryString(getCurrent().getRequest()
								.getQueryString());
				if (queryStringParameterMap != null) {
					parameterMap.putAll(queryStringParameterMap);
				}
				Action action = _urlMapperMap.get(urlMapper);
				executor = new ActionExecutor(action, parameterMap);
				break;
			}
		}
		if (executor != null) {
			this._handleResult(executor);
			remove();
			return true;
		}
		remove();
		return false;
	}

	public UrlMapper[] getUrlMapperArray() {
		return _urlMapperArray;
	}

	public void destroy() {
		this._request = null;
		this._response = null;
		this._servletContext = null;
	}

	public HttpServletRequest getRequest() {
		return _request;
	}

	public HttpServletResponse getResponse() {
		return _response;
	}

	public ServletContext getServletContext() {
		return _servletContext;
	}
}
