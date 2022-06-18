/**
 * @Title: ApiProxy.java
 * @version V1.0
 */
package com.lckp.jproxy.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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

import com.lckp.jproxy.config.FormatConfig;
import com.lckp.jproxy.constant.Proxy;
import com.lckp.jproxy.constant.ProxyType;
import com.lckp.jproxy.constant.SeriesType;
import com.lckp.jproxy.model.FormatRule;
import com.lckp.jproxy.model.FormatType;
import com.lckp.jproxy.model.Regular;
import com.lckp.jproxy.param.ProxyParam;
import com.lckp.jproxy.util.ApplicationContextHolder;
import com.lckp.jproxy.util.FormatUtil;

/**
 * @className: ApiProxy
 * @description: 接口代理
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
public class ApiProxy {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiProxy.class);
	
	public static FormatType formatType = null;
	
	/**
	 * 
	 * @param request
	 * @param proxyParam
	 * @return 代理所需参数
	 * @description: 获取代理所需参数
	 */
	public ProxyParam getProxyParam(HttpServletRequest request, ProxyParam proxyParam) {
		proxyParam.setPath(request.getServletPath());
		
		Map<String, String[]> paramMap = request.getParameterMap();
		StringBuilder paramBuilder = new StringBuilder();
		String searchKeyValue = null;
		proxyParam.setFormatRule(formatType.getAnime());
		
		for (String key : paramMap.keySet()) {
			String value = paramMap.get(key)[0];
			if (Proxy.SERIES_TYPE_FIELD.equals(key)) {
				if(SeriesType.SERIAL.equals(SeriesType.valueOf(value.toUpperCase()))) {
					proxyParam.setFormatRule(formatType.getSerial());
					proxyParam.setSeriesType(SeriesType.SERIAL);
				}
				continue;
			}
			if (proxyParam.getSearchKeyField().equals(key)) {	
				searchKeyValue = value;
				continue;
			}
			paramBuilder.append("&" + key + "=" + value);
		}
			
		String paramString = paramBuilder.substring(1);
		if (!paramString.startsWith("?")) {
			paramString = "?" + paramString;
		}
		proxyParam.setParamString(paramString);
		proxyParam.setSearchKeyValue(FormatUtil.formatSearchKey(searchKeyValue, proxyParam.getFormatRule().getSearch()));
		return proxyParam;
	}
	
	/**
	 * 
	 * @param response
	 * @param xml
	 * @param proxyParam
	 * @throws IOException
	 * @description: 返回处理结果
	 */
	public void responseToSonarr(HttpServletResponse response, String xml, ProxyParam proxyParam) throws IOException {
		try {
			FormatConfig formatConfig = (FormatConfig) ApplicationContextHolder.getBean("formatConfig");
			LOGGER.debug("格式化开关状态：{}", formatConfig.isEnable());
			if (formatConfig.isEnable()) {
				xml = FormatUtil.formatXml(xml, proxyParam.getFormatRule());
			}
			
		} catch (Exception e) {
			LOGGER.error("Format Error: {}", e);
		}
		response.getOutputStream().write(xml.getBytes());
	}
	
	/**
	 * 
	 * @param xml
	 * @return 去除 Sxx 的标题
	 * @throws DocumentException
	 * @description: 判断是否用标题查询
	 */
	public String tryNewSearchKey(String xml, ProxyParam proxyParam) throws Exception {
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
			String searchKeyValue = proxyParam.getSearchKeyValue();
			if (Pattern.matches(".* S\\d+ \\d+$", searchKeyValue)) {
				searchKeyValue = searchKeyValue.replaceAll(" S\\d+ \\d+$", "");
				proxyParam.setSearchKeyValue(searchKeyValue);
				return get(proxyParam);
			}
		}
		return xml;
	}
	
	
	/**
	 * 
	 * @return body
	 * @description: get请求
	 */
	public String get(ProxyParam proxyParam) {
		String paramString = proxyParam.getParamString();
		if (StringUtils.isNotBlank(proxyParam.getSearchKeyValue())) {
			paramString = paramString + "&" + proxyParam.getSearchKeyField() + "=" + proxyParam.getSearchKeyValue();
		}
				
		LOGGER.info("path: {}", proxyParam.getPath());
		LOGGER.info("paramString: {}", paramString.replaceAll("apikey=[a-zA-Z0-9]+&", "apikey=******&"));
		String url = proxyParam.getUrl() + proxyParam.getPath() + paramString;
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);
		
		String xml = null;
		if (ProxyType.PROWLARR.equals(proxyParam.getProxyType())) {
			xml = new String(respEntity.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		} else {
			xml = respEntity.getBody();
		}
		
		return xml;
	}
	
	/**
	 * 
	 * @param formatConfig
	 * @description: 初始化规则
	 */
	public static void initFormatRule(FormatConfig formatConfig) {
		formatType = new FormatType();
		
		// Anime
		FormatRule anime = new FormatRule();
		if (formatConfig.getCustom() != null && formatConfig.getCustom().getAnime() != null) {
			anime.setSearch(merge(formatConfig.getCustom().getAnime().getSearch(), formatConfig.getOfficial().getAnime().getSearch()));
			anime.setCommon(merge(formatConfig.getCustom().getAnime().getCommon(), formatConfig.getOfficial().getAnime().getCommon()));
			anime.setAccurate(merge(formatConfig.getCustom().getAnime().getAccurate(), formatConfig.getOfficial().getAnime().getAccurate()));
			formatType.setAnime(anime);
			
		} else {
			formatType.setAnime(formatConfig.getOfficial().getAnime());
			LOGGER.debug("custom anime regular is null");
		}
		LOGGER.info("初始化动漫规则完毕");


		// Serial
		FormatRule serial = new FormatRule();
		if (formatConfig.getCustom() != null && formatConfig.getCustom().getSerial() != null) {
			serial.setSearch(merge(formatConfig.getCustom().getSerial().getSearch(), formatConfig.getOfficial().getSerial().getSearch()));
			serial.setCommon(merge(formatConfig.getCustom().getSerial().getCommon(), formatConfig.getOfficial().getSerial().getCommon()));
			serial.setAccurate(merge(formatConfig.getCustom().getSerial().getAccurate(), formatConfig.getOfficial().getSerial().getAccurate()));
			formatType.setSerial(serial);

		} else {
			formatType.setSerial(formatConfig.getOfficial().getSerial());
			LOGGER.debug("custom serial regular is null");
		}
		LOGGER.info("初始化电视剧规则完毕");
	}
	
	/**
	 * 
	 * @param regulars1
	 * @param regulars2
	 * @return 
	 * @description: 合并规则
	 */
	private static List<Regular> merge(List<Regular> regulars1, List<Regular> regulars2){
		if (regulars1 == null || regulars1.size() == 0) {
			return regulars2;
		}
		
		List<Regular> regulars = new ArrayList<Regular>();
		for (Regular regular : regulars1) {
			regulars.add(regular);
		}
		for (Regular regular : regulars2) {
			regulars.add(regular);
		}
		return regulars;
	}
}
