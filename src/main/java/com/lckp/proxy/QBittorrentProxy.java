/**
 * @Title: QBittorrentProxy.java
 * @version V1.0
 */
package com.lckp.proxy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.lckp.config.JProxyConfiguration;
import com.lckp.util.FormatUtil;

/**
 * @className: QBittorrentProxy
 * @description: qBittorrent代理
 * @date 2022年8月4日
 * @author LuckyPuppy514
 */
public class QBittorrentProxy {
	private static final Logger LOGGER = LoggerFactory.getLogger(QBittorrentProxy.class);
	
	private final static String NAME_FIELD = "&dn=";
	private final static String RENAME_FIELD = "&rename=";
	private final static String COOKIE_FIELD = "cookie";
	private final static String SID_FIELD = "SID";
	
	private final static String TORRENTS_ADD_PATH = "/api/v2/torrents/add";
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @description: 添加种子并重命名
	 */
	public void addTorrentAndRename(HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, IOException {
		Map<String, String[]> paramMap = request.getParameterMap();
		StringBuffer buffer = new StringBuffer();			
		for (String key : paramMap.keySet()) {
			String value = paramMap.get(key)[0];
			if (value.contains(NAME_FIELD)) {
				int si = value.indexOf(NAME_FIELD) + 4;
				String prefix = value.substring(0, si);
				String title = value.substring(si);
				int ei = title.indexOf("&");
				title = title.substring(0, ei);
				String suffix =  value.substring(si + ei);
				title = URLDecoder.decode(title, StandardCharsets.UTF_8.toString());
				title = FormatUtil.format(title, JProxyConfiguration.resultRuleList);
				title = URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
				value = prefix + title + suffix;
				buffer.append(RENAME_FIELD + title);
			}
			buffer.append("&" + key + "=" + value);
		}
		String paramString = buffer.replace(0, 1, "?").toString();
		LOGGER.debug("addTorrentAndRename paramString: {}", paramString);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			if(headerName.equals(COOKIE_FIELD)) {
				headerValue = headerValue.replaceAll(SID_FIELD, ";" + SID_FIELD);
			}
			headers.add(headerName, headerValue);
		}
		
		RequestEntity<String> requestEntity = new RequestEntity<String>(headers, HttpMethod.GET,
				new URI(getRequestUrl(TORRENTS_ADD_PATH, paramString)));
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> respEntity = restTemplate.exchange(requestEntity, String.class);
		response.getOutputStream().write(respEntity.getBody().getBytes());
	}
	
	/**
	 * 
	 * @param path
	 * @param paramString
	 * @return
	 * @description: 获取请求地址
	 */
	private String getRequestUrl(String path, String paramString) {
		StringBuffer buffer = new StringBuffer();
		if (!JProxyConfiguration.qBittorrent.getProxyIp().startsWith("http")) {
			buffer.append("http://");
		}
		buffer.append(JProxyConfiguration.qBittorrent.getProxyIp());
		buffer.append(":" + JProxyConfiguration.qBittorrent.getProxyPort());
		buffer.append(path);
		buffer.append(paramString);
		return buffer.toString();
	}
}
