package com.lckp.jproxy.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lckp.jproxy.constant.ApiField;
import com.lckp.jproxy.service.ISystemUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 登陆拦截器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-23
 */
@Slf4j
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

	private final ISystemUserService systemUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("登陆拦截器: {}", request.getServletPath());
		String token = request.getHeader(ApiField.HEADER_TOKEN);
		if (systemUserService.verify(token)) {
			log.debug("token: {}", token.substring(0, 12) + "******" + token.substring(token.length() - 5));
			return true;
		}
		response.sendError(HttpStatus.FORBIDDEN.value());
		return false;
	}
}
