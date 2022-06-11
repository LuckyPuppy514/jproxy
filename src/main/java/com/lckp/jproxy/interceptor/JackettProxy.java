/**
 * @Title: JackettProxy.java
 * @version V1.0
 */
package com.lckp.jproxy.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lckp.jproxy.config.JackettConfig;
import com.lckp.jproxy.model.Rule;
import com.lckp.jproxy.util.ApplicationContextHolder;
import com.lckp.jproxy.util.FormatUtil;

/**
 * @className: JackettProxy
 * @description: Jackett 接口代理
 * @date 2022年6月10日
 * @author LuckyPuppy514
 */
public class JackettProxy implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(JackettProxy.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		JackettConfig jackettConfig = (JackettConfig) ApplicationContextHolder.getBean("jackettConfig");

		LOGGER.info("proxy by jproxy: {}", jackettConfig.getUrl());
		String path = request.getServletPath();
		LOGGER.info("path: {}", path);
		if(!path.startsWith("/api")) {
			response.getOutputStream().write("jproxy running".getBytes());
			return false;
		}

		Map<String, String[]> paramMap = request.getParameterMap();
		StringBuilder paramBuilder = new StringBuilder();
		Rule rule = jackettConfig.getFormat().getAnime();
		String type = "anime";
		for (String key : paramMap.keySet()) {
			if (key.equals("type") && paramMap.get(key)[0].equals("serial")) {
				rule = jackettConfig.getFormat().getSerial();
				type = "serial";
				continue;
			}
			
			paramBuilder.append("&" + key + "=" + paramMap.get(key)[0]);
		}
		String paramString = "?" + paramBuilder.substring(1);
		LOGGER.info("type: {}", type);
		LOGGER.info("paramString: {}", paramString.replaceAll("apikey=[a-zA-Z0-9]+&", "apiKey=******&"));

		// 请求 jackett 接口
		String url = jackettConfig.getUrl() + path + paramString;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);

		// 格式化标题后在返回 sonarr
		String xml = respEntity.getBody();
		String respXml = xml;
		if (jackettConfig.getFormat().isEnable()) {
			try {
				respXml = FormatUtil.format(xml, rule);
			} catch (Exception e) {
				LOGGER.error("Format Error: {}", e);
			}
		}
		
		response.getOutputStream().write(respXml.getBytes());
		return false;
	}
}
