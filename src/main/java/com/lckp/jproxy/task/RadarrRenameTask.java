package com.lckp.jproxy.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lckp.jproxy.constant.ApiField;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.constant.Messages;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.constant.TableField;
import com.lckp.jproxy.constant.Token;
import com.lckp.jproxy.entity.RadarrRule;
import com.lckp.jproxy.entity.RadarrTitle;
import com.lckp.jproxy.service.IQbittorrentService;
import com.lckp.jproxy.service.IRadarrRuleService;
import com.lckp.jproxy.service.IRadarrTitleService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.util.FormatUtil;

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
	private final IRadarrTitleService radarrTitleService;

	private final IRadarrRuleService radarrRuleService;

	private final ISystemConfigService systemConfigService;

	private final IQbittorrentService qbittorrentService;

	private final RestTemplate restTemplate;

	private final CacheManager cacheManager;

	@Scheduled(cron = "${time.radarr-rename}")
	public void run() {
		try {
			log.debug("开始执行 Radarr 重命名任务");
			if (!qbittorrentService.isLogin()) {
				log.debug("qBittorrent 未登录");
				return;
			}
			// 获取下载队列
			StringBuilder url = new StringBuilder(
					systemConfigService.queryValueByKey(SystemConfigKey.RADARR_URL));
			url.append("/api/v3/queue/details?" + ApiField.RADARR_APIKEY);
			url.append("=" + systemConfigService.queryValueByKey(SystemConfigKey.RADARR_APIKEY));
			JSONArray jsonArray = JSON
					.parseArray(restTemplate.getForEntity(url.toString(), String.class).getBody());
			if (jsonArray != null && !jsonArray.isEmpty()) {
				String format = systemConfigService.queryValueByKey(SystemConfigKey.RADARR_DOWNLOADER_FORMAT);
				Map<String, List<RadarrRule>> tokenRuleMap = new ConcurrentHashMap<>();
				Matcher matcher = Pattern.compile(Token.REGEX).matcher(format);
				while (matcher.find()) {
					String token = matcher.group(1);
					if (Token.TITLE.equals(token) || Token.SEASON.equals(token)
							|| Token.LANGUAGE.equals(token) || Token.RESOLUTION.equals(token)
							|| Token.QUALITY.equals(token)) {
						continue;
					}
					tokenRuleMap.put(token, radarrRuleService.query(token));
				}
				jsonArray.forEach(object -> {
					try {
						// 重命名种子
						JSONObject json = (JSONObject) object;
						Integer movieId = json.getInteger(ApiField.RADARR_MOVIE_ID);
						List<RadarrTitle> radarrTitleList = radarrTitleService.query()
								.eq(TableField.MOVIE_ID, movieId).list();
						if (!radarrTitleList.isEmpty()) {
							RadarrTitle radarrTitle = radarrTitleList.get(0);
							String title = radarrTitle.getMainTitle();
							Integer year = radarrTitle.getYear();
							String hash = json.getString(ApiField.RADARR_DOWNLOAD_ID).toLowerCase();
							if (cacheManager.getCache(CacheName.RADARR_RENAME).get(hash) == null) {
								String language = ((JSONObject) json.getJSONArray(ApiField.RADARR_LANGUAGES)
										.get(0)).getString(ApiField.RADARR_NAME);
								String quality = json.getJSONObject(ApiField.RADARR_QUALITY)
										.getJSONObject(ApiField.RADARR_QUALITY)
										.getString(ApiField.RADARR_NAME);
								String oldTorrentName = json.getString(ApiField.RADARR_TITLE);
								String newTorrentName = FormatUtil.replaceToken(Token.TITLE, title, format);
								newTorrentName = FormatUtil.replaceToken(Token.YEAR, String.valueOf(year),
										newTorrentName);
								newTorrentName = FormatUtil.replaceToken(Token.LANGUAGE, "[" + language + "]",
										newTorrentName);
								newTorrentName = FormatUtil.replaceToken(Token.RESOLUTION,
										"[" + quality + "]", newTorrentName);
								newTorrentName = FormatUtil.removeToken(Token.QUALITY, newTorrentName);
								List<String> files = qbittorrentService.files(hash);
								// 种子重命名
								newTorrentName = radarrTitleService.format(oldTorrentName, newTorrentName,
										tokenRuleMap);
								newTorrentName = FormatUtil.removeAllToken(newTorrentName).trim();
								qbittorrentService.rename(hash, newTorrentName);
								log.info("种子重命名：{} => {}", oldTorrentName, newTorrentName);
								// 文件重命名
								String newFolderPath = title + "(" + year + ")";
								for (String oldFilePath : files) {
									int startIndex = oldFilePath.lastIndexOf("/") + 1;
									String oldFileName = oldFilePath.substring(startIndex);
									String newFileName = oldFileName;
									Matcher fileNameMatcher = Pattern.compile(Common.VIDEO_EXTENSION_REGEX)
											.matcher(oldFileName);
									if (fileNameMatcher.find()) {
										String extension = fileNameMatcher.group(1);
										newFileName = fileNameMatcher.replaceAll("");
										newFileName = newTorrentName + "." + extension;
									}
									String newFilePath = newFolderPath + "/" + newFileName;
									qbittorrentService.renameFile(hash, oldFilePath, newFilePath.trim());
									log.info("文件重命名：{} => {}", oldFileName, newFileName);
								}
								cacheManager.getCache(CacheName.RADARR_RENAME).put(hash, 1);
							}
						}
					} catch (Exception e) {
						log.error("qBittorrent 重命名出错（单个）：{}", e);
					}
				});
			}
		} catch (Exception e) {
			if (e.getMessage().contains(Messages.SYSTEM_CONFIG_INVALID_PREFIX)) {
				log.debug("qBittorrent 重命名出错：{}", e.getMessage());
			} else {
				log.error("qBittorrent 重命名出错：{}", e);
			}
		} finally {
			log.debug("执行 Radarr 重命名任务完毕");
		}
	}
}
