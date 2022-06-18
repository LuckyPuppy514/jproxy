/**
 * @Title: CommonInterceptor.java
 * @version V1.0
 */
package com.lckp.jproxy.interceptor;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @className: CommonInterceptor
 * @description: 
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
public class CommonInterceptor implements HandlerInterceptor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LOGGER.info("common interceptor: {}", request.getServletPath());
		printSystemInfo(response.getOutputStream());
		return false;
	}
	
	private void printSystemInfo(OutputStream out) throws IOException {
		String systemInfo = "        _   ____                                \r\n"
				+ "       | | |  _ \\   _ __    ___   __  __  _   _ \r\n"
				+ "    _  | | | |_) | | '__|  / _ \\  \\ \\/ / | | | |\r\n"
				+ "   | |_| | |  __/  | |    | (_) |  >  <  | |_| |\r\n"
				+ "    \\___/  |_|     |_|     \\___/  /_/\\_\\  \\__, |\r\n"
				+ "                                          |___/  2022 @LuckyPuppy514";
		
		out.write(systemInfo.getBytes());
	}
}