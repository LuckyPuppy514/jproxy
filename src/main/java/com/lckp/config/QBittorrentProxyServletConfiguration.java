/**
 * @Title: QBittorrentProxyServletConfiguration.java
 * @version V1.0
 */
package com.lckp.config;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lckp.proxy.QBittorrentProxyServlet;

/**
 * @className: QBittorrentProxyServletConfiguration
 * @description: qBittorrent 代理服务配置
 * @date 2022年8月4日
 * @author LuckyPuppy514
 */
@Configuration
public class QBittorrentProxyServletConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(QBittorrentProxyServletConfiguration.class);

	@Bean
	public static ServletRegistrationBean<QBittorrentProxyServlet> servletRegistrationBean() {
		ServletRegistrationBean<QBittorrentProxyServlet> qBittorrentProxyServlet = new ServletRegistrationBean<QBittorrentProxyServlet>(
				new QBittorrentProxyServlet(), "/qbittorrent/*");
		LOGGER.info("IS DEBUG ENABLED: " + LOGGER.isDebugEnabled());
		qBittorrentProxyServlet.addInitParameter(ProxyServlet.P_TARGET_URI, "http://127.0.0.1:8080");
		qBittorrentProxyServlet.addInitParameter(ProxyServlet.P_LOG, LOGGER.isDebugEnabled() ? "true" : "false");
		return qBittorrentProxyServlet;
	}
}
