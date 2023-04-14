package com.lckp.jproxy.task;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.service.IQbittorrentService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.service.ITransmissionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 下载器登录定时任务
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-04-11
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class DownloaderLoginTask implements CommandLineRunner {

	private final IQbittorrentService qbittorrentService;

	private final ITransmissionService transmissionService;

	private final ISystemConfigService systemConfigService;

	@Scheduled(cron = "${time.downloader-login}")
	public void run() {
		if (qbittorrentService.login()) {
			log.debug("qBittorrent 已重新登录");
		}
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			if (qbittorrentService.login(systemConfigService.queryValueByKey(SystemConfigKey.QBITTORRENT_URL),
					systemConfigService.queryValueByKey(SystemConfigKey.QBITTORRENT_USERNAME),
					systemConfigService.queryValueByKey(SystemConfigKey.QBITTORRENT_PASSWORD))) {
				log.info("qBittorrent 已登录");
			}
		} catch (Exception e) {
			log.debug("qBittorrent 登录失败：{}", e.getMessage());
		}
		try {
			if (transmissionService.login(
					systemConfigService.queryValueByKey(SystemConfigKey.TRANSMISSION_URL),
					systemConfigService.queryValueByKey(SystemConfigKey.TRANSMISSION_USERNAME),
					systemConfigService.queryValueByKey(SystemConfigKey.TRANSMISSION_PASSWORD))) {
				log.info("Transmission 已登录");
			}
		} catch (Exception e) {
			log.debug("Transmission 登录失败：{}", e.getMessage());
		}
	}
}
