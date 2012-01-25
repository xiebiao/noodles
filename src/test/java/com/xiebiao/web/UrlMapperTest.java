package com.xiebiao.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

public class UrlMapperTest extends TestCase {
	private UrlMapper urlMapper;
	private List<String> parameterNames;
	private String requestUrl;

	public void setUp() {
		parameterNames = new ArrayList<String>();
		StringBuilder sb = new StringBuilder("/user");
		parameterNames.add("post");
		parameterNames.add("id");
		for (int i = 0; i < parameterNames.size(); i++) {
			sb.append("/${" + parameterNames.get(i) + "}");
		}
		urlMapper = new UrlMapper(sb.toString());
		requestUrl = "/user/a_a/1asd";
	}

	// @Test
	// public void test_init() {
	// String url = "/user/${id}";
	// // String url = "/user/1";
	// UrlMapper urlMapper = new UrlMapper(url);
	// }

	@Test
	public void test_get_parameter_map() {
		Map map = this.urlMapper.getParameterMap(requestUrl);
		for (int i = 0; i < parameterNames.size(); i++) {
			System.out.println(parameterNames.get(i) + ":"
					+ map.get(parameterNames.get(i)));
		}
		Assert.assertNotNull(map);
	}
}
