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
	private String _url;
	private Pattern _pattern;
	private String[] _parameterNames;

	public UrlMapper(String mappingUrl) {
		this._url = mappingUrl;
		if (LOG.isDebugEnabled()) {
			LOG.debug(this._url);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("^");
		String tempUrl = this._url;
		List<String> parameterNames = new ArrayList<String>();
		for (; tempUrl.length() > 0;) {
			int $ = tempUrl.indexOf("${");
			if ($ != -1) {
				sb.append(tempUrl.substring(0, $));
				tempUrl = tempUrl.substring($ + 2);
				sb.append("(\\w*)");
				int $_ = tempUrl.indexOf("}");
				String paramName = tempUrl.substring(0, $_);
				parameterNames.add(paramName);
				tempUrl = tempUrl.substring($_ + 1);
				continue;
			}
			sb.append(tempUrl);
			break;
		}
		sb.append("$");
		this._parameterNames = new String[parameterNames.size()];
		for (int i = 0; i < parameterNames.size(); i++) {
			this._parameterNames[i] = parameterNames.get(i);
		}
		this._pattern = Pattern.compile(sb.toString());
	}

	/**
	 * Get param
	 * 
	 * @param uri
	 * @return
	 */
	public Map<String, String> getParameterMap(String uri) {
		Map<String, String> paramsValues = new HashMap<String, String>();
		// Url equals.
		if (uri.equals(this.getUrl())) {
			return paramsValues;
		}
		Matcher m = this._pattern.matcher(uri);
		if (!m.matches()) {
			return null;
		}
		int count = m.groupCount();
		if (this._parameterNames.length != count) {
			return null;
		}
		for (int i = 1; i <= count; i++) {
			String value = m.group(i);
			paramsValues.put(this._parameterNames[i - 1], value);
		}
		return paramsValues;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof UrlMapper) {
			UrlMapper urlMapper = (UrlMapper) obj;
			if (urlMapper._url.equals(this._url)) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return _url.hashCode();
	}

	public String getUrl() {
		return _url;
	}

	public Pattern getPattern() {
		return _pattern;
	}
}
