/**
 * @Title: LoginInterceptor.java
 * @version V1.0
 */
package com.lckp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lckp.config.JProxyConfiguration;
import com.lckp.constant.Field;
import com.lckp.constant.Page;

/**
 * @className: LoginInterceptor
 * @description: 登录拦截器
 * @date 2022年7月18日
 * @author LuckyPuppy514
 */
public class LoginInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String path = request.getServletPath();
		LOGGER.debug("登录拦截器：{}", path);

		if (!JProxyConfiguration.isInit()) {
			response.getOutputStream().write("Loading ... Please wait a minute.".getBytes());
			return false;
		}
		
		// 登录状态校验
		if (!checkLogin(request)) {
			response.sendRedirect("/" + Page.LOGIN);
			return false;
		}

		return true;
	}
	
	/**
	 * 
	 * @param request
	 * @param username 用户名
	 * @description: 设置登录状态为已登录
	 */
	public static void login(HttpServletRequest request, String username) {
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(30 * 60);
		session.setAttribute(Field.USERNAME, username);	
	}
	
	/**
	 * 
	 * @param request
	 * @description: 注销
	 */
	public static void logout(HttpServletRequest request) {
		request.getSession().setAttribute(Field.USERNAME, null);	
	}
	
	/**
	 * 
	 * @param request
	 * @return 已登录：true，未登录：false
	 * @description: 登录状态校验
	 */
	public static boolean checkLogin(HttpServletRequest request) {
		String username = (String) request.getSession().getAttribute(Field.USERNAME);
		LOGGER.debug("登录校验：{}", username);
		if (StringUtils.isBlank(username)) {
			return false;
		}
		return true;
	}
}
