package com.xiebiao.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;

public class UrlMapper {
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this
			.getClass());
	private String url;
	private Pattern pattern;
	private String[] parameterNames;

	public UrlMapper(String mappingUrl) {
		this.url = mappingUrl;
		LOG.debug(this.url);
		StringBuilder sb = new StringBuilder();
		sb.append("^");
		String tempUrl = this.url;
		List<String> parameterNames = new ArrayList<String>();
		for (; tempUrl.length() != 0;) {
			int $ = tempUrl.indexOf("${");
			if ($ != -1) {
				sb.append(tempUrl.substring(0, $));
				tempUrl = tempUrl.substring($ + 2);
				sb.append("(\\w*)");
				int $_ = tempUrl.indexOf("}");
				String paramName = tempUrl.substring(0, $_);
				parameterNames.add(paramName);
				tempUrl = tempUrl.substring($_ + 1);
			} else {
				// URL中没有映射参数
				sb.append(this.url);
				break;
			}
		}
		sb.append("$");
		this.parameterNames = new String[parameterNames.size()];
		for (int i = 0; i < parameterNames.size(); i++) {
			this.parameterNames[i] = parameterNames.get(i);
		}
		
		this.pattern = Pattern.compile(sb.toString());
	}

	/**
	 * 从请求URL种获取参数值
	 * 
	 * @param requestUrl
	 * @return
	 */
	public Map<String, Object> getParameterMap(String requestUrl) {
		Matcher m = this.pattern.matcher(requestUrl);
		if (!m.matches()) {
			LOG.debug(requestUrl + " can't matche");
			return null;
		}
		Map<String, Object> paramsValues = new HashMap<String, Object>();
		int count = m.groupCount();
		if (this.parameterNames.length != count) {
			return null;
		}
		for (int i = 1; i <= count; i++) {
			String value = m.group(i);
			LOG.debug(this.parameterNames[i-1]+":"+value);
			paramsValues.put(this.parameterNames[i-1], value);
		}
		return paramsValues;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof UrlMapper) {
			UrlMapper urlMapper = (UrlMapper) obj;
			if (urlMapper.url.equals(this.url)) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return url.hashCode();
	}
}
