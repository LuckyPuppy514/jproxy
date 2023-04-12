package com.lckp.jproxy.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.service.IQbittorrentService;
import com.lckp.jproxy.service.ISystemConfigService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 下载器重新登录定时任务
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-04-11
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class DownloaderReLoginTask {

	private final IQbittorrentService qbittorrentService;

	private final ISystemConfigService systemConfigService;

	@Scheduled(cron = "${time.downloader-reLogin}")
	public void run() {
		if (qbittorrentService.isLogin()) {
			qbittorrentService.login(systemConfigService.queryValueByKey(SystemConfigKey.QBITTORRENT_URL),
					systemConfigService.queryValueByKey(SystemConfigKey.QBITTORRENT_USERNAME),
					systemConfigService.queryValueByKey(SystemConfigKey.QBITTORRENT_PASSWORD));
			log.debug("qBittorrent 已重新登录");
		}
	}
}
