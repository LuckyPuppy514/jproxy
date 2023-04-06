package com.lckp.jproxy.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lckp.jproxy.service.ISonarrRuleService;
import com.lckp.jproxy.service.ISonarrTitleService;
import com.lckp.jproxy.service.ITmdbTitleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Sonarr 同步定时任务
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-29
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class SonarrSyncTask {

	private final ISonarrTitleService sonarrTitleService;

	private final ISonarrRuleService sonarrRuleService;

	private final ITmdbTitleService tmdbTitleService;

	@Scheduled(cron = "${time.sync-sonarr-title}")
	public void syncSonarrTitle() {
		try {
			log.info("开始同步剧集标题");
			sonarrTitleService.sync();
			tmdbTitleService.sync(sonarrTitleService.queryNeedSyncTmdbTitle());
			log.info("同步剧集标题完毕");
		} catch (Exception e) {
			log.error("同步剧集标题出错：{}", e.getMessage());
		}

	}

	@Scheduled(cron = "${time.sync-sonarr-rule}")
	public void syncSonarrRule() {
		try {
			log.info("开始同步剧集规则");
			sonarrRuleService.sync();
			log.info("同步剧集规则完毕");
		} catch (Exception e) {
			log.error("同步剧集规则出错：{}", e.getMessage());
		}
	}
}
