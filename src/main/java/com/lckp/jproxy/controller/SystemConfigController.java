package com.lckp.jproxy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.lckp.jproxy.config.CharonConfig;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.entity.SystemConfig;
import com.lckp.jproxy.service.ISystemCacheService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.util.Generator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 系统配置
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-12
 */
@Slf4j
@Tag(name = "系统配置")
@RequestMapping("/api/system/config")
@RestController
@RequiredArgsConstructor
public class SystemConfigController implements CommandLineRunner {

	private final ISystemConfigService systemConfigService;

	private final ISystemCacheService systemCacheService;

	private final CharonConfig charonConfig;

	private final RestTemplate restTemplate;

	@Value("${rules.location}")
	private String rulesLocation;

	@Operation(summary = "查询")
	@GetMapping("/query")
	public ResponseEntity<List<SystemConfig>> query() {
		return ResponseEntity.ok(systemConfigService.query().list());
	}

	@Operation(summary = "更新")
	@PostMapping("/update")
	public ResponseEntity<Void> update(@RequestBody List<SystemConfig> systemConfigList) {
		systemConfigService.updateSystemConfig(systemConfigList);
		charonConfig.updateAllServerUrl();
		systemCacheService.clear(CacheName.SONARR_TITLE_SYNC_INTERVAL);
		systemCacheService.clear(CacheName.TMDB_TITLE_SYNC_INTERVAL);
		systemCacheService.clear(CacheName.RADARR_TITLE_SYNC_INTERVAL);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "查询作者列表")
	@GetMapping("/author/list")
	public ResponseEntity<String[]> listAuthor() {
		String authorUrl = Generator.generateAuthorUrl(rulesLocation);
		String[] authorList = { "LuckyPuppy514" };
		try {
			authorList = restTemplate.getForEntity(authorUrl, String[].class).getBody();
		} catch (Exception e) {
			log.error("获取作者列表出错：{}", e.getMessage());
		}
		return ResponseEntity.ok(authorList);
	}

	/**
	 * @param args
	 * @throws Exception
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) throws Exception {
		charonConfig.updateAllServerUrl();
	}
}
