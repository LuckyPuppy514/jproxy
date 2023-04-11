package com.lckp.jproxy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lckp.jproxy.service.IQbittorrentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * qBittorrent 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-04-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QbittorrentServiceImpl implements IQbittorrentService {

	private final RestTemplate restTemplate;

	private String cookie = null;

	private String url = null;

	private boolean isLogin = false;

	/**
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @see com.lckp.jproxy.service.IQbittorrentService#login(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean login(String url, String username, String password) {
		this.url = url;
		this.cookie = null;
		this.isLogin = false;
		StringBuilder api = new StringBuilder(url);
		api.append("/api/v2/auth/login");
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("username", username);
		params.add("password", password);
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params);
		ResponseEntity<String> response = restTemplate.postForEntity(api.toString(), request, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			HttpHeaders headers = response.getHeaders();
			headers.forEach((headerName, values) -> {
				if (headerName.equalsIgnoreCase(HttpHeaders.SET_COOKIE)) {
					cookie = values.get(0);
					isLogin = true;
				}
			});
		}
		return StringUtils.isNotBlank(cookie);
	}

	/**
	 * @param hash
	 * @return
	 * @see com.lckp.jproxy.service.IQbittorrentService#files(java.lang.String)
	 */
	@Override
	public List<String> files(String hash) {
		StringBuilder api = new StringBuilder();
		api.append(url);
		api.append("/api/v2/torrents/files");
		api.append("?hash=" + hash);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.COOKIE, cookie);
		HttpEntity<String> request = new HttpEntity<>(headers);
		String body = restTemplate.exchange(api.toString(), HttpMethod.GET, request, String.class).getBody();
		log.debug("body: {}", body);
		JSONArray jsonArray = JSON.parseArray(body);
		List<String> fileList = new ArrayList<>(jsonArray.size());
		jsonArray.forEach(object -> fileList.add(((JSONObject) object).getString("name")));
		return fileList;
	}

	/**
	 * @param hash
	 * @param name
	 * @return
	 * @see com.lckp.jproxy.service.IQbittorrentService#rename(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean rename(String hash, String name) {
		StringBuilder api = new StringBuilder();
		api.append(url);
		api.append("/api/v2/torrents/rename");
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("hash", hash);
		params.add("name", name);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", cookie);
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(api.toString(), request, String.class);
		return response.getStatusCode().is2xxSuccessful();
	}

	/**
	 * @param hash
	 * @param oldPath
	 * @param newPath
	 * @return
	 * @see com.lckp.jproxy.service.IQbittorrentService#renameFile(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean renameFile(String hash, String oldPath, String newPath) {
		StringBuilder api = new StringBuilder();
		api.append(url);
		api.append("/api/v2/torrents/renameFile");
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("hash", hash);
		params.add("oldPath", oldPath);
		params.add("newPath", newPath);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", cookie);
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(api.toString(), request, String.class);
		return response.getStatusCode().is2xxSuccessful();
	}

	/**
	 * @return
	 * @see com.lckp.jproxy.service.IDownloaderService#isLogin()
	 */
	@Override
	public boolean isLogin() {
		return this.isLogin;
	}
}
