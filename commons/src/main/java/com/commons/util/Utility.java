package com.commons.util;

import java.util.List;

public class Utility {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String toLowerCase(String s) {
		return s == null ? null : s.trim().toLowerCase();
	}

	public static <T> boolean isObjectNull(T obj) {
		return obj == null;
	}

	public static <T> boolean isEmptyList(List<T> list) {
		return list == null || list.size() == 0;
	}

}
