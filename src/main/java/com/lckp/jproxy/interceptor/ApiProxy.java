/**
 * @Title: ApiProxy.java
 * @version V1.0
 */
package com.lckp.jproxy.interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lckp.jproxy.config.ApiConfig;
import com.lckp.jproxy.model.ApiType;
import com.lckp.jproxy.model.Rule;
import com.lckp.jproxy.model.SeriesType;
import com.lckp.jproxy.util.ApplicationContextHolder;
import com.lckp.jproxy.util.FormatUtil;

/**
 * @className: ApiProxy
 * @description: 接口代理
 * @date 2022年6月11日
 * @author LuckyPuppy514
 */
public class ApiProxy implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiProxy.class);

	// 搜索关键字字段名
	private static final String SEARCH_KEY = "q";
	// 系列类型字段名
	private static final String SERIES_TYPE = "type";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ApiConfig apiConfig = (ApiConfig) ApplicationContextHolder.getBean("apiConfig");

		String path = request.getServletPath();
		LOGGER.info("proxy path: {}", path);
		if ("/".equals(path)) {
			response.getOutputStream().write("jproxy running".getBytes());
			return false;
		}

		// 获取转发参数
		Map<String, String[]> paramMap = request.getParameterMap();
		StringBuilder paramBuilder = new StringBuilder();
		Rule rule = apiConfig.getFormat().getAnime();
		SeriesType seriesType = SeriesType.ANIME;
		String searchKey = null;
		for (String key : paramMap.keySet()) {
			String value = paramMap.get(key)[0];
			if (SERIES_TYPE.equals(key)) {
				if(SeriesType.SERIAL.equals(SeriesType.valueOf(value.toUpperCase()))) {
					rule = apiConfig.getFormat().getSerial();
					seriesType = SeriesType.SERIAL;
				}
				continue;
			}
			if (SEARCH_KEY.equals(key)) {
				searchKey = value;
				continue;
			}
			paramBuilder.append("&" + key + "=" + value);
		}

		String paramString = paramBuilder.substring(1);
		if (!paramString.startsWith("?")) {
			paramString = "?" + paramString;
		}
		LOGGER.info("seriesType: {}", seriesType);

		ApiType apiType;
		if (path.startsWith("/api")) {
			apiType = ApiType.JACKETT;
		} else {
			apiType = ApiType.PROWLARR;
		}
		String xml = get(apiConfig, apiType, path, paramString, searchKey);

		try {
			// 无结果且关键字类似：标题 S2 02，尝试只用标题搜索
			searchKey = getNewSearchKey(xml, searchKey);
			if (null != searchKey) {
				xml = get(apiConfig, apiType, path, paramString, searchKey);
			}

			// 格式化标题
			if (apiConfig.getFormat().isEnable()) {
				xml = FormatUtil.format(xml, rule);
			}
		} catch (Exception e) {
			LOGGER.error("Format Error: {}", e);
		}

		response.getOutputStream().write(xml.getBytes());
		return false;
	}

	/**
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 * @description: 判断是否用标题查询
	 */
	private static String getNewSearchKey(String xml, String searchKey) throws DocumentException {
		if (StringUtils.isBlank(xml)) {
			return null;
		}
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element channel = root.element("channel");

		/*
		 * 如果查询结果为空，且搜索关键字类似：标题 S2 02，尝试去除：S2 02，只查询标题 原因：sonarr
		 * 查询，只会查询当前季对应集数（比如：02），或季+集数（比如：S2 02） 导致某些采用绝对集数的数据（比如：古见同学 S2
		 * 02，幻樱字幕组，使用绝对集数：14） 无法被 sonarr 搜索到
		 */
		if (channel != null && channel.element("item") == null) {
			if (Pattern.matches(".* S\\d+ \\d+$", searchKey)) {
				searchKey = searchKey.replaceAll(" S\\d+ \\d+$", "");
				return searchKey;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @return body
	 * @description: get 请求
	 */
	private String get(ApiConfig apiConfig, ApiType apiType, String path, String paramString, String searchKey) {
		if(null != searchKey) {
			paramString = paramString + "&" + SEARCH_KEY + "=" + searchKey;
		}

		RestTemplate restTemplate = new RestTemplate();
		String xml;
		String url;

		if (ApiType.JACKETT == apiType) {
			url = apiConfig.getJackett();
			ResponseEntity<String> respEntity = restTemplate.getForEntity(url + path + paramString, String.class);
			xml = respEntity.getBody();

		} else {
			url = apiConfig.getProwlarr();
			ResponseEntity<String> respEntity = restTemplate.getForEntity(url + path + paramString, String.class);
			xml = new String(respEntity.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		}

		LOGGER.info("proxy url: {}", url);
		LOGGER.info("paramString: {}", paramString.replaceAll("apikey=[a-zA-Z0-9]+&", "apikey=******&"));

		return xml;
	}
}
