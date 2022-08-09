/**
 * @Title: IndexerProxy.java
 * @version V1.0
 */
package com.lckp.proxy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.lckp.config.JProxyConfiguration;
import com.lckp.constant.Field;
import com.lckp.constant.ProxyType;
import com.lckp.constant.SearchType;
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
		LOGGER.info("url: {}{}", proxyParam.getProxyUrl(), proxyParam.getProxyPath());
		LOGGER.info("searchKey: {}", proxyParam.getSearchKey());
		LOGGER.info("paramString: {}", proxyParam.getParamString().replaceAll("apikey=[a-zA-Z0-9]+&", "apikey=******&"));
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
		Map<String, String[]> paramMap = new HashMap<>(request.getParameterMap());

		// 获取查询关键字
		String[] values = paramMap.get(Field.SEARCH_KEY);
		if (null != values) {
			proxyParam.setSearchKey(values[0]);
			paramMap.remove(Field.SEARCH_KEY);
		}
		// 获取查询类型
		values = paramMap.get(Field.SEARCH_TYPE);
		if (null != values) {
			String searchType = values[0];
			proxyParam.setSearchType(searchType);
			if(SearchType.tvsearch.toString().equals(searchType)) {
				// 获取季
				values = paramMap.get(Field.SEASON);
				if (null != values) {
					proxyParam.setSeason(values[0]);
					paramMap.remove(Field.SEASON);
				}
				// 获取集
				values = paramMap.get(Field.EP);
				if (null != values) {
					proxyParam.setEp(values[0]);
					paramMap.remove(Field.EP);
				}
			}
		}
		
		// 其他字段
		StringBuffer buffer = new StringBuffer();
		for (String key : paramMap.keySet()) {
			buffer.append("&" + key + "=" + paramMap.get(key)[0]);
		}
		String paramString = buffer.replace(0, 1, "?").toString();
		proxyParam.setParamString(paramString);
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
}
