package com.lckp.jproxy.service.impl;

import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lckp.jproxy.constant.ApiField;
import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.service.ITransmissionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Transmission 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-04-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransmissionServiceImpl implements ITransmissionService {

	private final RestTemplate restTemplate;

	private String url;

	private String authorization = null;

	private String session = null;

	private boolean isLogin = false;

	/**
	 * 
	 * 登录
	 *
	 * @param url
	 * @param authorization
	 * @return boolean
	 */
	private boolean login(String url, String authorization) {
		this.url = url;
		this.authorization = authorization;
		this.session = null;
		this.isLogin = false;
		JSONObject arguments = new JSONObject();
		arguments.put("fields", new String[] { "version" });
		JSONObject params = new JSONObject();
		params.put(ApiField.TRANSMISSION_METHOD, "session-get");
		params.put(ApiField.TRANSMISSION_ARGUMENTS, arguments);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(authorization);
		HttpEntity<JSONObject> request = new HttpEntity<>(params, headers);
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			if (HttpStatusCode.valueOf(409).equals(response.getStatusCode())) {
				headers = response.getHeaders();
				headers.forEach((headerName, values) -> {
					if (headerName.equalsIgnoreCase(ApiField.TRANSMISSION_SESSION_ID)) {
						this.session = values.get(0);
						this.isLogin = StringUtils.isNotBlank(session);
					}
				});
			}
		} catch (Exception e) {
			log.debug("Transmission 登录出错：{}", e.getMessage());
		}
		return this.isLogin;
	}

	/**
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @see com.lckp.jproxy.service.IDownloaderService#login(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean login(String url, String username, String password) {
		return login(url, Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
	}

	/**
	 * 
	 * @return
	 * @see com.lckp.jproxy.service.IDownloaderService#login()
	 */
	@Override
	public boolean login() {
		return login(this.url, this.authorization);
	}

	/**
	 * @return
	 * @see com.lckp.jproxy.service.IDownloaderService#isLogin()
	 */
	@Override
	public boolean isLogin() {
		return isLogin;
	}

	/**
	 * @param hash
	 * @return
	 * @see com.lckp.jproxy.service.IDownloaderService#files(java.lang.String)
	 */
	@Override
	public List<String> files(String hash) {
		return null;
	}

	/**
	 * @param hash
	 * @param name
	 * @return
	 * @see com.lckp.jproxy.service.IDownloaderService#rename(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean rename(String hash, String name) {
		name = name.replace(":", "_");
		String oldName = getTorrentName(hash);
		if (StringUtils.isBlank(oldName) || oldName.equalsIgnoreCase(hash)) {
			log.debug("Transmission 种子暂时无法重命名：{}", oldName);
			return false;
		}
		Matcher matcher = Pattern.compile(Common.VIDEO_AND_SUBTITLE_EXTENSION_REGEX).matcher(oldName);
		if (matcher.find()) {
			String extension = matcher.group(1);
			name = name + extension;
		}
		if (oldName.equals(name)) {
			log.debug("Transmission 种子已经重命名：{}", name);
			return false;
		}
		JSONObject arguments = new JSONObject();
		arguments.put("ids", hash);
		arguments.put("path", oldName);
		arguments.put("name", name);
		JSONObject params = new JSONObject();
		params.put(ApiField.TRANSMISSION_METHOD, "torrent-rename-path");
		params.put(ApiField.TRANSMISSION_ARGUMENTS, arguments);
		log.debug("params: {}", JSON.toJSONString(params));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(authorization);
		headers.add(ApiField.TRANSMISSION_SESSION_ID, session);
		HttpEntity<JSONObject> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		if (HttpStatusCode.valueOf(409).equals(response.getStatusCode()) && login()) {
			headers.remove(ApiField.TRANSMISSION_SESSION_ID);
			headers.add(ApiField.TRANSMISSION_SESSION_ID, session);
			response = restTemplate.postForEntity(url, request, String.class);
		}
		log.debug("body: {}", response.getBody());
		if (response.getStatusCode().is2xxSuccessful()) {
			log.info("Transmission 种子重命名成功：{} => {}", oldName, name);
			return true;
		}
		return false;
	}

	/**
	 * @param hash
	 * @param oldPath
	 * @param newPath
	 * @return
	 * @see com.lckp.jproxy.service.IDownloaderService#renameFile(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean renameFile(String hash, String oldPath, String newPath) {
		return false;
	}

	/**
	 * 
	 * 根据 hash 获取种子名称
	 *
	 * @param hash
	 * @return String
	 */
	private String getTorrentName(String hash) {
		JSONObject arguments = new JSONObject();
		arguments.put("ids", hash);
		arguments.put("fields", new String[] { "name" });
		JSONObject params = new JSONObject();
		params.put(ApiField.TRANSMISSION_METHOD, "torrent-get");
		params.put(ApiField.TRANSMISSION_ARGUMENTS, arguments);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(authorization);
		headers.add(ApiField.TRANSMISSION_SESSION_ID, session);
		HttpEntity<JSONObject> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(this.url, request, String.class);
		if (HttpStatusCode.valueOf(409).equals(response.getStatusCode()) && login()) {
			headers.remove(ApiField.TRANSMISSION_SESSION_ID);
			headers.add(ApiField.TRANSMISSION_SESSION_ID, session);
			response = restTemplate.postForEntity(url, request, String.class);
		}
		log.debug("body: {}", response.getBody());
		if (response.getStatusCode().is2xxSuccessful()) {
			arguments = JSON.parseObject(response.getBody()).getJSONObject(ApiField.TRANSMISSION_ARGUMENTS);
			if (arguments != null) {
				JSONArray torrents = arguments.getJSONArray(ApiField.TRANSMISSION_TORRENTS);
				if (torrents != null && !torrents.isEmpty()) {
					return ((JSONObject) torrents.get(0)).getString(ApiField.TRANSMISSION_NAME);
				}
			}
		}
		return null;
	}
}
