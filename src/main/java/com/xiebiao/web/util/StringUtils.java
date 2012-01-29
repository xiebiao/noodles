package com.xiebiao.web.util;

/**
 * 
 * @author xiaog (joyrap@gmail.com)
 * 
 */
public final class StringUtils {
	public static String firstLetterUpperCase(String str) {
		String first = str.substring(0, 1);
		String end = str.substring(1);
		return first.toUpperCase() + end;
	}
}
