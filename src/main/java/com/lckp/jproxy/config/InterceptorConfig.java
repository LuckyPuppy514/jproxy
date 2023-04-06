package com.lckp.jproxy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.interceptor.LoginInterceptor;
import com.lckp.jproxy.service.ISystemUserService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 拦截器配置
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-23
 */
@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

	private final ISystemUserService systemUserService;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor(systemUserService))
				.addPathPatterns(Common.INTERCEPTOR_ALL_PATH)
				.excludePathPatterns(Common.INTERCEPTOR_SONARR_API_PATH)
				.excludePathPatterns(Common.INTERCEPTOR_RADARR_API_PATH)
				.excludePathPatterns(Common.INTERCEPTOR_LOGIN_API_PATH)
				.excludePathPatterns(Common.INTERCEPTOR_LOGIN_PAGE_PATH)
				.excludePathPatterns(Common.INTERCEPTOR_INDEX_PAGE_PATH)
				.excludePathPatterns(Common.INTERCEPTOR_STATIC_PATH)
				.excludePathPatterns(Common.INTERCEPTOR_KNFIE4J_PATHS)
				.excludePathPatterns(Common.INTERCEPTOR_OTHER_PATHS);
	}
}
