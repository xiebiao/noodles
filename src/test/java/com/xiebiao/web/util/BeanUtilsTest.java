package com.xiebiao.web.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.xiebiao.web.action.Home;

public class BeanUtilsTest extends TestCase {
	@Test
	public void test_set_properties() {
		Home bean = new Home();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "1");
		map.put("name", "xiaog");

		try {
			BeanUtils.setProperties(bean, map);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertNotNull(null);
		}
		Assert.assertEquals("xiaog", bean.getName());
		Assert.assertEquals(1, bean.getId());
	}

	@Test
	public void test_set_property() {
		Home bean = new Home();
		try {
			BeanUtils.setProperty(bean, "id", "1");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertNotNull(null);
		}
		Assert.assertEquals(1, bean.getId());
	}
}
