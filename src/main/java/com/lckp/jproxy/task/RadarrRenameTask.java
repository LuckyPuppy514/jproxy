package com.lckp.jproxy.task;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lckp.jproxy.constant.ApiField;
import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.constant.Downloader;
import com.lckp.jproxy.constant.Messages;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.service.IQbittorrentService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.service.ITransmissionService;

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

	private final ITransmissionService transmissionService;

	private final RestTemplate restTemplate;

	@Value("${time.radarr-rename-fall-back}")
	private int fallBackTime;

	@Value("${rename.file}")
	private boolean renameFile;

	@Scheduled(cron = "${time.radarr-rename}")
	public synchronized void run() {
		try {
			log.debug("开始执行 Radarr 重命名任务");
			if (!qbittorrentService.isLogin() && !transmissionService.isLogin()) {
				log.debug("下载器未登录");
				return;
			}
			// 获取 fallBackTime 分钟之前到现在的抓取记录
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - fallBackTime);
			SimpleDateFormat dateFormat = new SimpleDateFormat(Common.DATE_TIME_FORMAT);
			dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of(ApiField.RADARR_TZ)));
			StringBuilder url = new StringBuilder(
					systemConfigService.queryValueByKey(SystemConfigKey.RADARR_URL));
			url.append("/api/v3/history/since?eventType=1");
			url.append("&" + ApiField.RADARR_DATE + "=" + dateFormat.format(calendar.getTime()));
			url.append("&" + ApiField.RADARR_APIKEY + "="
					+ systemConfigService.queryValueByKey(SystemConfigKey.RADARR_APIKEY));
			String body = restTemplate.getForEntity(url.toString(), String.class).getBody();
			JSONArray jsonArray = JSON.parseArray(body);
			if (jsonArray != null && !jsonArray.isEmpty()) {
				jsonArray.forEach(object -> {
					String torrentInfoHash = null;
					try {
						JSONObject json = (JSONObject) object;
						torrentInfoHash = json.getString(ApiField.RADARR_DOWNLOAD_ID);
						if (StringUtils.isNotBlank(torrentInfoHash)) {
							torrentInfoHash = torrentInfoHash.toLowerCase();
						} else {
							torrentInfoHash = json.getJSONObject(ApiField.RADARR_DATA)
									.getString(ApiField.RADARR_TORRENT_INFO_HASH);
						}
						String sourceTitle = json.getString(ApiField.RADARR_SOURCES_TITLE);
						if (StringUtils.isNotBlank(torrentInfoHash)) {
							// 种子重命名
							String downloadClient = json.getJSONObject(ApiField.RADARR_DATA)
									.getString(ApiField.RADARR_DOWNLOAD_CLIENT);
							if (Downloader.TRANSMISSION.getName().equalsIgnoreCase(downloadClient)) {
								transmissionService.rename(torrentInfoHash, sourceTitle);
							} else {
								if (qbittorrentService.rename(torrentInfoHash, sourceTitle)) {
									if (renameFile) {
										boolean renamed = false;
										List<String> files = qbittorrentService.files(torrentInfoHash);
										for (String oldFilePath : files) {
											int startIndex = oldFilePath.lastIndexOf("/") + 1;
											if (startIndex > 0 && sourceTitle
													.equals(oldFilePath.substring(0, startIndex - 1))) {
												log.debug("qBittorrent 文件已经重命名: {}", oldFilePath);
												renamed = true;
												break;
											}
											String oldFileName = oldFilePath.substring(startIndex);
											String newFileName = oldFileName;
											Matcher extensionMatcher = Pattern
													.compile(Common.VIDEO_EXTENSION_REGEX)
													.matcher(oldFileName);
											if (extensionMatcher.find()) {
												String extension = extensionMatcher.group(1);
												newFileName = sourceTitle + extension;
											}
											String newFilePath = sourceTitle + "/" + newFileName;
											qbittorrentService.renameFile(torrentInfoHash, oldFilePath,
													newFilePath);
											log.info("qBittorrent 文件重命名成功：{} => {}", oldFileName,
													newFileName);
										}
										if (!renamed) {
											log.info("qBittorrent 种子重命名成功：{} => {}", torrentInfoHash,
													sourceTitle);
										}
									} else {
										log.debug("已关闭文件重命名");
									}
								} else {
									log.debug("qBittorrent 种子重命名失败：{} => {}", torrentInfoHash, sourceTitle);
								}
							}
						}
					} catch (Exception e) {
						if (e.getMessage().contains("Not Found")) {
							log.debug("下载器重命名出错（单个）：{}", e.getMessage());
						} else {
							log.error("下载器重命名出错（单个）：{}", e);
						}
					}
				});
			}
		} catch (Exception e) {
			if (e.getMessage().contains(Messages.SYSTEM_CONFIG_INVALID_PREFIX)) {
				log.debug("下载器重命名出错：{}", e.getMessage());
			} else {
				log.error("下载器重命名出错：{}", e);
			}
		}
		log.debug("执行 Radarr 重命名任务完毕");
	}
}
