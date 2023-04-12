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

	@Scheduled(cron = "${time.radarr-title-sync}")
	public void syncRadarrTitle() {
		try {
			radarrTitleService.sync();
		} catch (Exception e) {
			log.error("同步电影标题出错：{}", e.getMessage());
		}
	}

	@Scheduled(cron = "${time.radarr-rule-sync}")
	public void syncRadarrRule() {
		try {
			radarrRuleService.sync();
		} catch (Exception e) {
			log.error("同步电影规则出错：{}", e.getMessage());
		}
	}
}
