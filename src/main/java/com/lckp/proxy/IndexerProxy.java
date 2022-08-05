/**
 * @Title: IndexerProxy.java
 * @version V1.0
 */
package com.lckp.proxy;

import java.io.IOException;
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

import com.lckp.config.JProxyConfiguration;
import com.lckp.constant.Field;
import com.lckp.constant.ProxyType;
import com.lckp.model.ProxyConfig;
import com.lckp.param.ProxyParam;
import com.lckp.util.FormatUtil;

/**
 * @className: IndexerProxy
 * @description: 索引器代理
 * @date 2022年8月4日
 * @author LuckyPuppy514
 */
public class IndexerProxy {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexerProxy.class);
	
	/**
	 * 
	 * @param proxyConfig
	 * @param proxyParam
	 * @param request
	 * @throws Exception 
	 * @description: 代理之前，获取请求地址，路径，参数
	 */
	public void preProxy(ProxyConfig proxyConfig, ProxyParam proxyParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		proxyParam = getRequestUrl(proxyConfig, proxyParam);
		proxyParam = getRequestParam(proxyParam, request);
		LOGGER.info("url: {}{}, searchkey: {}", proxyParam.getProxyUrl(), proxyParam.getProxyPath(), proxyParam.getSearchKeyValue());
		
		// 格式化请求参数
		String searchKey = FormatUtil.search(proxyParam.getSearchKeyValue(), JProxyConfiguration.searchRuleList);
		proxyParam.setSearchKeyValue(searchKey);
		
		proxy(proxyParam, response);
		
	}
	
	/**
	 * 
	 * @param proxyParam
	 * @param response
	 * @throws Exception 
	 * @description: 代理请求
	 */
	public void proxy(ProxyParam proxyParam, HttpServletResponse response) throws Exception {
		String resultXml = get(proxyParam);
		// 有水友的 Sonarr 会根据绝对集数搜索，所以先注释掉
		// resultXml = trySearchWithoutSeasonAndEpisode(resultXml, proxyParam);
		afterProxy(resultXml, response);
	}
	
	/**
	 * 
	 * @param resultXml
	 * @param response
	 * @throws IOException
	 * @throws DocumentException 
	 * @description: 处理请求结果并返回
	 */
	public void afterProxy(String resultXml, HttpServletResponse response) throws IOException, DocumentException {
		resultXml = FormatUtil.result(resultXml, JProxyConfiguration.resultRuleList);
		response.getOutputStream().write(resultXml.getBytes());
	}
	
	/**
	 * 
	 * @param proxyConfig
	 * @param proxyParam
	 * @return
	 * @description: 获取请求地址
	 */
	public ProxyParam getRequestUrl(ProxyConfig proxyConfig, ProxyParam proxyParam) {
		StringBuffer buffer = new StringBuffer();
		if (!proxyConfig.getProxyIp().startsWith("http")) {
			buffer.append("http://");
		}
		buffer.append(proxyConfig.getProxyIp());
		buffer.append(":" + proxyConfig.getProxyPort());
		proxyParam.setProxyUrl(buffer.toString());
		return proxyParam;
	}
	
	/**
	 * 
	 * @param proxyParam
	 * @param request
	 * @return 
	 * @description: 获取所有请求参数
	 */
	public ProxyParam getRequestParam(ProxyParam proxyParam, HttpServletRequest request) {		
		Map<String, String[]> paramMap = request.getParameterMap();
		StringBuffer buffer = new StringBuffer();
		String searchKeyValue = null;
		
		for (String key : paramMap.keySet()) {
			String value = paramMap.get(key)[0];
			if (proxyParam.getSearchKeyField().equals(key)) {	
				searchKeyValue = value;
				continue;
			}
			buffer.append("&" + key + "=" + value);
		}
		String paramString = buffer.replace(0, 1, "?").toString();
		
		proxyParam.setParamString(paramString);
		proxyParam.setSearchKeyValue(searchKeyValue);
		return proxyParam;
	}
	
	/**
	 * 
	 * @param proxyParam
	 * @return
	 * @description: get 请求
	 */
	public String get(ProxyParam proxyParam) {
		StringBuffer buffer = new StringBuffer();
		if (StringUtils.isNotBlank(proxyParam.getSearchKeyValue())) {
			buffer.append(proxyParam.getParamString());
			buffer.append("&" + proxyParam.getSearchKeyField() + "=" + proxyParam.getSearchKeyValue());
			proxyParam.setParamString(buffer.toString());
		}
		
		LOGGER.debug("Proxy Url: {}", proxyParam.getProxyUrl());
		LOGGER.debug("Proxy Path: {}", proxyParam.getProxyPath());
		LOGGER.debug("Proxy Param: {}", proxyParam.getParamString().replaceAll("apikey=[a-zA-Z0-9]+&", "apikey=******&"));
		
		buffer = new StringBuffer();
		buffer.append(proxyParam.getProxyUrl());
		buffer.append(proxyParam.getProxyPath());
		buffer.append(proxyParam.getParamString());
		String url = buffer.toString();
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);
		
		String xml = null;
		if (ProxyType.Prowlarr.equals(proxyParam.getProxyType())) {
			xml = new String(respEntity.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		} else {
			xml = respEntity.getBody();
		}
		LOGGER.debug("Proxy Result: {}", xml);
		return xml;
	}
	
	/**
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 * @description: 尝试去除季集信息，只用标题查询
	 */
	public String trySearchWithoutSeasonAndEpisode(String resultXml, ProxyParam proxyParam) throws Exception {
		if (StringUtils.isBlank(resultXml)) {
			return null;
		}
		Document document = DocumentHelper.parseText(resultXml);
		Element root = document.getRootElement();
		Element channel = root.element(Field.RESP_CHANNEL);

		/*
		 * 如果查询结果为空，且非第一季，尝试只用标题查询
		 * 原因：Sonarr 没有使用绝对集数查询，会导致只含绝对集数的结果不能被查询到
		 */
		if (channel != null && channel.element(Field.RESP_ITEM) == null) {
			String searchKeyValue = proxyParam.getSearchKeyValue();
			if (Pattern.matches(".* S\\d+ \\d+$", searchKeyValue)) {
				searchKeyValue = searchKeyValue.replaceAll(" S\\d+ \\d+$", "");
				proxyParam.setSearchKeyValue(searchKeyValue);
				return get(proxyParam);
			}
		}
		return resultXml;
	}
}
