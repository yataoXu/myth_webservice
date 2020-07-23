package com.zdmoney.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public final class WebUtils {

	private WebUtils() {
	}

	/**
	 * 清空cookie
	 * @param response
	 * @param path
	 * @param cookieName
	 */
	public static void clearCookie(HttpServletResponse response,
			String path, String cookieName) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	
	/**
	 * 增加cookie值(not httponly)
	 * @param response
	 * @param cookiename
	 * @param cookievalue
	 * @param path
	 * @param maxSecond
	 */
	public static void addCookieNoHttpOnly(HttpServletResponse response,
			String cookiename, String cookievalue, String path, int maxSecond) {
		Cookie cookie = new Cookie(cookiename, cookievalue);
		cookie.setPath(path);
		cookie.setMaxAge(maxSecond);
		response.addCookie(cookie);
	}

	/**
	 * 获取cookie
	 * @param request
	 * @param cookiename
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookiename) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookiename.equals(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}

	/**
	 * 获取cookie中值
	 * @param request
	 * @param cookiename
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request,
			String cookiename) {
		Cookie cookie = getCookie(request, cookiename);
		if (cookie == null) {
			return null;
		}
		return cookie.getValue();
	}
	
	/**
	 * 
	 * @param request
	 * @param pnames
	 * @return
	 */
	public static Map<String, String> getRequestParams(HttpServletRequest request, String[] pnames) {
		Map result = new TreeMap();
		if (pnames != null) {
			for (String pn : pnames) {
				String pv = request.getParameter(pn);
				if (!StringUtils.isNotBlank(pv)) {
					continue;
				}
				result.put(pn, pv);
			}
		}
		return result;
	}
	
	/**
	 * request参数->map
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestMap(HttpServletRequest request) {
		Map result = new HashMap();
		Enumeration it = request.getParameterNames();
		String key = null;
		while (it.hasMoreElements()) {
			key = (String)it.nextElement();
			result.put(key, request.getParameter(key));
		}
		return result;
	}
}
