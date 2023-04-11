package com.lckp.jproxy.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.filter.RadarrJackettFilter;
import com.lckp.jproxy.filter.RadarrProwlarrFilter;
import com.lckp.jproxy.filter.SonarrJackettFilter;
import com.lckp.jproxy.filter.SonarrProwlarrFilter;
import com.lckp.jproxy.service.IRadarrJackettService;
import com.lckp.jproxy.service.IRadarrProwlarrService;
import com.lckp.jproxy.service.ISonarrJackettService;
import com.lckp.jproxy.service.ISonarrProwlarrService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 过滤器配置
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-07
 */
@Configuration
@RequiredArgsConstructor
public class FilterConfig {

	private final ISonarrJackettService sonarrJackettService;

	private final ISonarrProwlarrService sonarrProwlarrService;

	private final IRadarrJackettService radarrJackettService;

	private final IRadarrProwlarrService radarrProwlarrService;

	@Bean
	FilterRegistrationBean<SonarrJackettFilter> sonarrJackettFilter() {
		FilterRegistrationBean<SonarrJackettFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new SonarrJackettFilter(sonarrJackettService));
		bean.setOrder(Integer.MIN_VALUE);
		bean.addUrlPatterns(Common.FILTER_SONARR_JACKETT_PATH);
		return bean;
	}

	@Bean
	FilterRegistrationBean<SonarrProwlarrFilter> sonarrProwlarrFilter() {
		FilterRegistrationBean<SonarrProwlarrFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new SonarrProwlarrFilter(sonarrProwlarrService));
		bean.setOrder(Integer.MIN_VALUE);
		bean.addUrlPatterns(Common.FILTER_SONARR_PROWLARR_PATH);
		return bean;
	}

	@Bean
	FilterRegistrationBean<RadarrJackettFilter> radarrJackettFilter() {
		FilterRegistrationBean<RadarrJackettFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new RadarrJackettFilter(radarrJackettService));
		bean.setOrder(Integer.MIN_VALUE);
		bean.addUrlPatterns(Common.FILTER_RADARR_JACKETT_PATH);
		return bean;
	}

	@Bean
	FilterRegistrationBean<RadarrProwlarrFilter> radarrProwlarrFilter() {
		FilterRegistrationBean<RadarrProwlarrFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new RadarrProwlarrFilter(radarrProwlarrService));
		bean.setOrder(Integer.MIN_VALUE);
		bean.addUrlPatterns(Common.FILTER_RADARR_PROWLARR_PATH);
		return bean;
	}
}