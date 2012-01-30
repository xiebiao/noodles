package com.xiebiao.web;

import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class UrlMapperTest extends TestCase {

	@Before
	public void setUp() {

	}

	public void test_create_1() {
		UrlMapper urlMapper = new UrlMapper("/user/${name}/${id}");
		Assert.assertNotNull(urlMapper);
	}

	public void test_create_2() {
		UrlMapper urlMapper = new UrlMapper("/user/${name}/${id}/");
		Assert.assertNotNull(urlMapper);
	}

	public void test_create_3() {
		UrlMapper urlMapper = new UrlMapper("/${user}/${name}/${id}/asdasd");
		Assert.assertNotNull(urlMapper);
	}

	@Test
	public void test_get_parameter_map() {
		UrlMapper urlMapper = new UrlMapper("/${user}/${name}/${id}/");
		Map<String, String> map = urlMapper.getParameterMap("/aa/bb/1/");
		Assert.assertNotNull(map);
		Assert.assertEquals(3, map.size());
	}
}
