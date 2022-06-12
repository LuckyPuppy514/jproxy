/**
 * @Title: ApiProxy.java
 * @version V1.0
 */
package com.lckp.jproxy.interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lckp.jproxy.config.ApiConfig;
import com.lckp.jproxy.model.Rule;
import com.lckp.jproxy.util.ApplicationContextHolder;
import com.lckp.jproxy.util.FormatUtil;

/**
 * @className: ApiProxy
 * @description: 接口代理
 * @date 2022年6月11日
 * @author LuckyPuppy514
 */
public class ApiProxy  implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiProxy.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ApiConfig apiConfig = (ApiConfig) ApplicationContextHolder.getBean("apiConfig");

		String path = request.getServletPath();
		LOGGER.info("path: {}", path);
		if("/".equals(path)) {
			response.getOutputStream().write("jproxy running".getBytes());
			return false;
		}
		
		// 获取转发参数
		Map<String, String[]> paramMap = request.getParameterMap();
		StringBuilder paramBuilder = new StringBuilder();
		Rule rule = apiConfig.getFormat().getAnime();
		String type = "anime";
		for (String key : paramMap.keySet()) {
			if ("type".equals(key) && "serial".equals(paramMap.get(key)[0])) {
				rule = apiConfig.getFormat().getSerial();
				type = "serial";
				continue;
			}
			
			paramBuilder.append("&" + key + "=" + paramMap.get(key)[0]);
		}
		String paramString = "?" + paramBuilder.substring(1);
		LOGGER.info("type: {}", type);
		LOGGER.info("paramString: {}", paramString.replaceAll("apikey=[a-zA-Z0-9]+&", "apikey=******&"));

		// 请求接口
		RestTemplate restTemplate = new RestTemplate();
		String xml = "";
		// jackett
		if(path.startsWith("/api")) {
			String url = apiConfig.getJackett() + path + paramString;
			ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);
			xml = respEntity.getBody();
			LOGGER.info("proxy jackett: {}", apiConfig.getJackett());
			
		// prowlarr
		} else {
			String url = apiConfig.getProwlarr() + path + paramString;
			ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);
			xml = new String(respEntity.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
			LOGGER.info("proxy prowlarr: {}", apiConfig.getProwlarr());
		}

		// 格式化标题
		String respXml = xml;
		if (apiConfig.getFormat().isEnable()) {
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
