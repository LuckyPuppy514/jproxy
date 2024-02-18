package com.lckp.jproxy.task;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
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
import com.lckp.jproxy.constant.Token;
import com.lckp.jproxy.entity.SonarrRule;
import com.lckp.jproxy.service.IQbittorrentService;
import com.lckp.jproxy.service.ISonarrRuleService;
import com.lckp.jproxy.service.ISonarrTitleService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.service.ITransmissionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Sonarr 重命名定时任务
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-04-11
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class SonarrRenameTask {
	private final ISonarrTitleService sonarrTitleService;

	private final ISonarrRuleService sonarrRuleService;

	private final ISystemConfigService systemConfigService;

	private final IQbittorrentService qbittorrentService;

	private final ITransmissionService transmissionService;

	private final RestTemplate restTemplate;

	@Value("${time.sonarr-rename-fall-back}")
	private int fallBackTime;

	@Value("${rename.file}")
	private boolean renameFile;

	@Scheduled(cron = "${time.sonarr-rename}")
	public synchronized void run() {
		try {
			log.debug("开始执行 Sonarr 重命名任务");
			if (!qbittorrentService.isLogin() && !transmissionService.isLogin()) {
				log.debug("下载器未登录");
				return;
			}
			// 获取 fallBackTime 分钟之前到现在的抓取记录
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - fallBackTime);
			SimpleDateFormat dateFormat = new SimpleDateFormat(Common.DATE_TIME_FORMAT);
			dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of(ApiField.SONARR_TZ)));
			StringBuilder url = new StringBuilder(
					systemConfigService.queryValueByKey(SystemConfigKey.SONARR_URL));
			url.append("/api/v3/history/since?eventType=1");
			url.append("&" + ApiField.SONARR_DATE + "=" + dateFormat.format(calendar.getTime()));
			url.append("&" + ApiField.SONARR_APIKEY + "="
					+ systemConfigService.queryValueByKey(SystemConfigKey.SONARR_APIKEY));
			String body = restTemplate.getForEntity(url.toString(), String.class).getBody();
			JSONArray jsonArray = JSON.parseArray(body);
			if (jsonArray != null && !jsonArray.isEmpty()) {
				Map<String, List<SonarrRule>> tokenRuleMap = new ConcurrentHashMap<>();
				tokenRuleMap.put(Token.SEASON, sonarrRuleService.query(Token.SEASON));
				tokenRuleMap.put(Token.EPISODE, sonarrRuleService.query(Token.EPISODE));
				Map<String, Integer> torrentInfoHashMap = new HashMap<>(jsonArray.size());
				jsonArray.forEach(object -> {
					try {
						JSONObject json = (JSONObject) object;
						String sourceTitle = json.getString(ApiField.SONARR_SOURCES_TITLE);
						String torrentInfoHash = json.getString(ApiField.RADARR_DOWNLOAD_ID);
						if (StringUtils.isNotBlank(torrentInfoHash)) {
							torrentInfoHash = torrentInfoHash.toLowerCase();
						} else {
							torrentInfoHash = json.getJSONObject(ApiField.SONARR_DATA)
									.getString(ApiField.SONARR_TORRENT_INFO_HASH);
						}
						if (StringUtils.isNotBlank(torrentInfoHash)
								&& torrentInfoHashMap.get(torrentInfoHash) == null) {
							torrentInfoHashMap.put(torrentInfoHash, 1);
							// 种子重命名
							String downloadClient = json.getJSONObject(ApiField.SONARR_DATA)
									.getString(ApiField.SONARR_DOWNLOAD_CLIENT);
							if (Downloader.TRANSMISSION.getName().equalsIgnoreCase(downloadClient)) {
								transmissionService.rename(torrentInfoHash, sourceTitle);
							} else {
								if (qbittorrentService.rename(torrentInfoHash, sourceTitle)) {
									if (renameFile) {
										int subtitleNo = 1;
										boolean renamed = false;
										// 文件重命名 SxxExx
										String newFileNameFormat = sonarrTitleService.format(sourceTitle,
												"{" + Token.SEASON + "}", tokenRuleMap);
										newFileNameFormat = newFileNameFormat + "{" + Token.EPISODE + "}";
										List<String> files = qbittorrentService.files(torrentInfoHash);
										for (String oldFilePath : files) {
											int lastIndex = oldFilePath.lastIndexOf("/") + 1;
											if (lastIndex > 0 && sourceTitle
													.equals(oldFilePath.substring(0, lastIndex - 1))) {
												log.debug("qBittorrent 文件已经重命名: {}", oldFilePath);
												renamed = true;
												break;
											}
											String oldFileName = oldFilePath.substring(lastIndex);
											String newFileName = oldFileName;
											Matcher extensionMatcher = Pattern
													.compile(Common.VIDEO_AND_SUBTITLE_EXTENSION_REGEX)
													.matcher(oldFileName);
											if (extensionMatcher.find()) {
												String extension = extensionMatcher.group(1);
												newFileName = extensionMatcher.replaceAll("");
												newFileName = sonarrTitleService
														.format(newFileName, newFileNameFormat, tokenRuleMap)
														.trim();
												if (StringUtils.isBlank(newFileName)
														|| !newFileName.matches("S\\d+E\\d+") || !newFileName.matches("(\\b|\\s)E\\d+")) {
													newFileName = oldFileName;
												} else {
													if (extension.matches(Common.SUBTITLE_EXTENSION_REGEX)) {
														newFileName = newFileName + "." + subtitleNo++;
													}
													newFileName = newFileName + " "
															+ sourceTitle.substring(sourceTitle.indexOf("["))
															+ extension;
												}
											}
											String newFilePath;
											int index = oldFilePath.indexOf("/");
											if (index + 1 == lastIndex) {
												newFilePath = sourceTitle + "/" + newFileName;
											} else {
												newFilePath = sourceTitle
														+ oldFilePath.substring(index, lastIndex)
														+ newFileName;
											}
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
		log.debug("执行 Sonarr 重命名任务完毕");
	}
}
