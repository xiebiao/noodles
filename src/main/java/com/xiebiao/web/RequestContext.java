package com.xiebiao.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
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
import com.xiebiao.web.exception.RenderException;
import com.xiebiao.web.renderer.Renderer;

/**
 * 
 * @author xiaog
 * 
 */
public class RequestContext {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext context;
	private boolean debug = true;
	private Map<UrlMapper, Action> urlMapperMap = new HashMap<UrlMapper, Action>();
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());
	private final static String ACTION_DEFAULT_PACKAGE = "com.xiebiao.web.action";
	private UrlMapper[] urlMapperArray;

	public RequestContext(Setting setting) {
		this.context = setting.getServletContext();
		this.debug = Boolean.parseBoolean(setting.getInitParameter("debug"));
		LOG.debug(setting.getInitParameter("debug"));
	}

	/**
	 * 
	 */
	public void init() {
		// 加载URL映射的Action
		_loadActions();

	}

	private void _loadActions() {
		// 可以与spring整合
		File actionClassFilePath = new File(this.getClass().getClassLoader()
				.getResource("").getFile()
				+ File.separator
				+ ACTION_DEFAULT_PACKAGE.replace(".", File.separator));
		String[] actionClassFiles = actionClassFilePath.list();
		if (this.isDebug()) {
			LOG.debug(actionClassFilePath.getAbsolutePath());
		}
		for (String _class : actionClassFiles) {
			String className = _class.replace(".class", "");
			try {
				Object actionObj = Class.forName(
						ACTION_DEFAULT_PACKAGE + "." + className).newInstance();
				Method[] ms = actionObj.getClass().getMethods();
				for (Method m : ms) {
					if (_hasAnnotation(m)) {
						Mapping mapping = m.getAnnotation(Mapping.class);
						String url = mapping.value();
						Action action = new Action(actionObj, m);
						this.urlMapperMap.put(new UrlMapper(url), action);
					}
				}

			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		urlMapperArray = new UrlMapper[this.urlMapperMap.keySet().size()];
		Iterator it = this.urlMapperMap.keySet().iterator();
		int sum = 0;
		while (it.hasNext()) {
			UrlMapper urlMapper = (UrlMapper) it.next();
			urlMapperArray[sum] = urlMapper;
			sum++;
		}
	}

	/**
	 * 检测Action方法注解Mapping的值
	 * 
	 * @param method
	 * @return
	 */
	private boolean _hasAnnotation(Method method) {
		Mapping mapping = method.getAnnotation(Mapping.class);
		if (mapping != null) {
			if (mapping.value() == null || mapping.value().equals("")) {
				if (this.isDebug()) {
					LOG.error(method.toGenericString() + ":"
							+ "has error mapping value");
				}
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	private void handleException(HttpServletRequest req, HttpServletResponse res) {

	}

	private void handleResult(Executor executor) {
		try {
			ActionContext.setActionContext(context, request, response);
			Object result = executor.excute();
			if (result instanceof Renderer) {
				Renderer renderer = (Renderer) result;
				try {
					if (response == null) {
						LOG.error("response is null");
					}
					renderer.render(context, request, response);
				} catch (RenderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ActionContext.removeActionContext();
		} catch (ExecuteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理请求
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
		String path = req.getContextPath();
		String url = req.getRequestURI().substring(path.length());
		Executor executor = null;
		for (UrlMapper urlMapper : this.urlMapperArray) {
			Map<String, Object> parameterMap = urlMapper.getParameterMap(url);
			if (parameterMap != null) {
				Action action = this.urlMapperMap.get(urlMapper);
				executor = new Executor(req, res, action, parameterMap);
				break;
			}
		}
		if (executor != null) {
			this.handleResult(executor);
			return true;
		}
		return false;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
