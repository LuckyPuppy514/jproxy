/**
 * @Title: MarketServerInterceptor.java
 * @version V1.0
 */
package com.lckp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @className: MarketServerInterceptor
 * @description: 规则市场拦截器
 * @date 2022年7月28日
 * @author LuckyPuppy514
 */
public class MarketServerInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(MarketServerInterceptor.class);
	
	private static final long EXPIRE = 1000 * 60 * 60 * 24;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		LOGGER.info("规则市场拦截器：{}, {}", request.getServletPath(), request.getRemoteHost());
		String timestamp = request.getHeader("timestamp");
		LOGGER.info("时间戳：{}", timestamp);
		// 简单校验时间戳
		if (timestamp == null || Math.abs(Long.parseLong(timestamp) - System.currentTimeMillis()) > EXPIRE) {
			return false;
		}
		
		return true;
	}
}
