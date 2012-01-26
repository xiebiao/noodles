package com.xiebiao.web.util;

public final class StringUtils {
	public static String firstLetterUpperCase(String str) {
		String first = str.substring(0, 1);
		String end = str.substring(1);
		return first.toUpperCase() + end;
	}
}
