package com.lckp.jproxy.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * <p>
 * 错误页面配置
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-30
 */
@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {

	/**
	 * @param registry
	 * @see org.springframework.boot.web.server.ErrorPageRegistrar#registerErrorPages(org.springframework.boot.web.server.ErrorPageRegistry)
	 */
	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
		registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
	}

}
