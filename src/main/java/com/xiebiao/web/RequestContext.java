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
import com.xiebiao.web.renderer.Renderer;

/**
 * 
 * @author xiaog
 * 
 */
public class RequestContext {
	public static String ENCODING = "UTF-8";
	private ServletContext context;
	private boolean debug = true;
	private Map<UrlMapper, Action> urlMapperMap = new HashMap<UrlMapper, Action>();
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());
	private UrlMapper[] urlMapperArray;
	private String packages;

	public RequestContext(Setting setting) {
		this.context = setting.getServletContext();
		this.debug = Boolean.parseBoolean(setting.getInitParameter("debug"));
		this.packages = setting.getInitParameter("packages");
		if (packages == null) {
			packages = "com.xiebiao.web.action";
		}
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
				+ File.separator + packages.replace(".", File.separator));
		String[] actionClassFiles = actionClassFilePath.list();
		if (this.isDebug()) {
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
						this.urlMapperMap.put(new UrlMapper(url), action);
					}
				}
			} catch (Exception e) {
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
		try {
			req.getRequestDispatcher("/WEB-INF/500.jsp").forward(req, res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleResult(ActionExecutor executor) {
		try {
			Object result = executor.excute();
			if (result instanceof Renderer) {
				Renderer renderer = (Renderer) result;
				renderer.render(ActionContext.getActionContext(), ActionContext
						.getActionContext().getRequest(), ActionContext
						.getActionContext().getResponse());

			} else if (result instanceof Redirector) {
				Redirector redirector = (Redirector) result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			handleException(ActionContext.getActionContext().getRequest(),
					ActionContext.getActionContext().getResponse());
		}
		ActionContext.removeActionContext();
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
		ActionContext.setActionContext(context, req, res);
		String path = req.getContextPath();
		String url = req.getRequestURI().substring(path.length());
		ActionExecutor executor = null;
		for (UrlMapper urlMapper : this.urlMapperArray) {
			Map<String, String> parameterMap = urlMapper.getParameterMap(url);
			if (parameterMap != null) {
				Action action = this.urlMapperMap.get(urlMapper);
				executor = new ActionExecutor(req, res, action, parameterMap);
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
