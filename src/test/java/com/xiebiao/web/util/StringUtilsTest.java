package com.xiebiao.web.util;

import junit.framework.Assert;
import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {
	public void test_first_letter_uppercase() {
		String str = "aasdA";
		String newStr = StringUtils.firstLetterUpperCase(str);
		Assert.assertEquals(str.substring(0, 1).toUpperCase(),
				newStr.substring(0, 1));
	}
}
