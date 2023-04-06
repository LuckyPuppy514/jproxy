package com.lckp.jproxy.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lckp.jproxy.service.IRadarrRuleService;
import com.lckp.jproxy.service.IRadarrTitleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Radarr 同步定时任务
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-29
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class RadarrSyncTask {

	private final IRadarrTitleService radarrTitleService;

	private final IRadarrRuleService radarrRuleService;

	@Scheduled(cron = "${time.sync-radarr-title}")
	public void syncRadarrTitle() {
		try {
			log.info("开始同步电影标题");
			radarrTitleService.sync();
			log.info("同步电影标题完毕");
		} catch (Exception e) {
			log.error("同步电影标题出错：{}", e.getMessage());
		}
	}

	@Scheduled(cron = "${time.sync-radarr-rule}")
	public void syncRadarrRule() {
		try {
			log.info("开始同步电影规则");
			radarrRuleService.sync();
			log.info("同步电影规则完毕");
		} catch (Exception e) {
			log.error("同步电影规则出错：{}", e.getMessage());
		}
	}
}
