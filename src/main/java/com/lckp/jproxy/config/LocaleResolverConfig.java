package com.lckp.jproxy.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 本地化配置
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-25
 */
@Configuration
public class LocaleResolverConfig implements LocaleResolver {

	@Override
	public Locale resolveLocale(HttpServletRequest httpServletRequest) {
		Locale locale = httpServletRequest.getLocale();
		locale = locale == null ? Locale.getDefault() : locale;
		return locale;
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		// do nothing
	}

	@Bean
	LocaleResolver localeResolver() {
		return new LocaleResolverConfig();
	}
}