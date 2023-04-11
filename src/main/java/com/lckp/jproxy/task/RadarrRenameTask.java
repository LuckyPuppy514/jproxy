package com.lckp.jproxy.task;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lckp.jproxy.constant.ApiField;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.constant.Messages;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.service.IQbittorrentService;
import com.lckp.jproxy.service.ISystemConfigService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Radarr 重命名定时任务
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-04-11
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class RadarrRenameTask {
	private final ISystemConfigService systemConfigService;

	private final IQbittorrentService qbittorrentService;

	private final RestTemplate restTemplate;

	private final CacheManager cacheManager;

	private static final long PAGE_SIZE = 30L;

	@Scheduled(cron = "${time.radarr-rename}")
	public synchronized void run() {
		try {
			log.debug("开始执行 Radarr 重命名任务");
			if (!qbittorrentService.isLogin()) {
				log.debug("qBittorrent 未登录");
				return;
			}
			// 获取抓取记录
			StringBuilder url = new StringBuilder(
					systemConfigService.queryValueByKey(SystemConfigKey.RADARR_URL));
			url.append("/api/v3/history?eventType=1&pageSize=" + PAGE_SIZE);
			url.append("&" + ApiField.RADARR_APIKEY + "="
					+ systemConfigService.queryValueByKey(SystemConfigKey.RADARR_APIKEY));
			String body = restTemplate.getForEntity(url.toString(), String.class).getBody();
			JSONArray jsonArray = JSONObject.parseObject(body).getJSONArray(ApiField.RADARR_RECORDS);
			if (jsonArray != null && !jsonArray.isEmpty()) {
				jsonArray.forEach(object -> {
					try {
						JSONObject json = (JSONObject) object;
						String sourceTitle = json.getString(ApiField.RADARR_SOURCES_TITLE);
						String torrentInfoHash = json.getString(ApiField.RADARR_DOWNLOAD_ID);
						if (StringUtils.isNotBlank(torrentInfoHash)) {
							torrentInfoHash = torrentInfoHash.toLowerCase();
						} else {
							torrentInfoHash = json.getJSONObject(ApiField.RADARR_DATA)
									.getString(ApiField.RADARR_TORRENT_INFO_HASH);
						}
						if (StringUtils.isNotBlank(torrentInfoHash) && cacheManager
								.getCache(CacheName.RADARR_RENAME).get(torrentInfoHash) == null) {
							cacheManager.getCache(CacheName.RADARR_RENAME).put(torrentInfoHash, 1);
							// 种子重命名
							qbittorrentService.rename(torrentInfoHash, sourceTitle);
							log.info("种子重命名：{} => {}", torrentInfoHash, sourceTitle);
							List<String> files = qbittorrentService.files(torrentInfoHash);
							for (String oldFilePath : files) {
								int startIndex = oldFilePath.lastIndexOf("/") + 1;
								String oldFileName = oldFilePath.substring(startIndex);
								String newFileName = oldFileName;
								Matcher fileNameMatcher = Pattern.compile(Common.VIDEO_EXTENSION_REGEX)
										.matcher(oldFileName);
								if (fileNameMatcher.find()) {
									String extension = fileNameMatcher.group(1);
									newFileName = sourceTitle + "." + extension;
								}
								String newFilePath = sourceTitle + "/" + newFileName;
								qbittorrentService.renameFile(torrentInfoHash, oldFilePath, newFilePath);
								log.info("文件重命名：{} => {}", oldFileName, newFileName);
							}
						}
					} catch (Exception e) {
						if (e.getMessage().contains("Not Found")) {
							log.debug("qBittorrent 重命名出错（单个）：{}", e.getMessage());
						} else {
							log.error("qBittorrent 重命名出错（单个）：{}", e);
						}
					}
				});
			}
		} catch (Exception e) {
			if (e.getMessage().contains(Messages.SYSTEM_CONFIG_INVALID_PREFIX)) {
				log.debug("qBittorrent 重命名出错：{}", e.getMessage());
			} else {
				log.error("qBittorrent 重命名出错：{}", e);
			}
		}
		log.debug("执行 Radarr 重命名任务完毕");
	}
}
