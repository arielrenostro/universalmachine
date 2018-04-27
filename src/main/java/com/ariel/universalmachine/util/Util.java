package com.ariel.universalmachine.util;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	
	private static NumberFormat numberFormat;

	public static boolean valoresDistintos(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return false;
		} else if (obj1 == null && obj2 != null) {
			return true;
		} else if (obj1 != null && obj2 == null) {
			return true;
		} else {
			return !obj1.equals(obj2);
		}
	}

	public static int countMatches(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return count;
	}

	public static <T> T getPrimeiroElemento(List<T> elementos) {
		if (null != elementos && 0 < elementos.size()) {
			return elementos.get(0);
		}
		return null;
	}

	public static <T> T getUltimoElemento(List<T> elementos) {
		if (null != elementos && 0 < elementos.size()) {
			return elementos.get(elementos.size() - 1);
		}
		return null;
	}

	public static String formatNumber(long numero) {
		return getNumberFormat().format(numero);
	}
	
	private static NumberFormat getNumberFormat() {
		if (null == numberFormat) {
			numberFormat = NumberFormat.getInstance();
		}
		return numberFormat;
	}

	public static boolean isNotEmpty(Map<?, ?> map) {
		return null != map && 0 < map.size();
	}
	
}
