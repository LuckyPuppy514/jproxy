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

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lckp.jproxy.constant.ApiField;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.entity.SystemConfig;
import com.lckp.jproxy.service.ISystemCacheService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.task.SonarrRenameTask;
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

	private final SonarrRenameTask sonarrRenameTask;

	private final RestTemplate restTemplate;

	@Value("${project.version}")
	private String projectVersion;

	@Value("${rule.location}")
	private String ruleLocation;

	@Value("${rule.location-backup}")
	private String ruleLocationBackup;

	private String latestVersion;

	@Operation(summary = "版本号")
	@GetMapping("/version")
	public ResponseEntity<String> version() {
		if (StringUtils.isBlank(latestVersion)) {
			try {
				latestVersion = JSON.parseObject(restTemplate.getForObject(
						"https://api.github.com/repos/LuckyPuppy514/jproxy/releases/latest", String.class))
						.getString(ApiField.GITHUB_TAG_NAME);
				if (StringUtils.isNotBlank(latestVersion)) {
					latestVersion = latestVersion.replace("v", "");
				}
			} catch (Exception e) {
				log.debug("获取最新版本号出错：{}", e.getMessage());
			}
		}
		if (StringUtils.isBlank(latestVersion) || (projectVersion).equals(latestVersion)) {
			return ResponseEntity.ok(projectVersion);
		}
		return ResponseEntity.ok(projectVersion + " 🚨");
	}

	@Operation(summary = "查询")
	@GetMapping("/query")
	public ResponseEntity<List<SystemConfig>> query() {
		sonarrRenameTask.run();
		return ResponseEntity.ok(systemConfigService.query().list());
	}

	@Operation(summary = "更新")
	@PostMapping("/update")
	public ResponseEntity<Void> update(@RequestBody List<SystemConfig> systemConfigList) {
		systemConfigService.updateSystemConfig(systemConfigList);
		systemCacheService.clear(CacheName.SONARR_TITLE_SYNC_INTERVAL);
		systemCacheService.clear(CacheName.TMDB_TITLE_SYNC_INTERVAL);
		systemCacheService.clear(CacheName.RADARR_TITLE_SYNC_INTERVAL);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "查询作者列表")
	@GetMapping("/author/list")
	public ResponseEntity<String[]> listAuthor() {
		String authorUrl = Generator.generateAuthorUrl();
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
		try {
			Generator.setRuleLocation(ruleLocation);
			restTemplate.getForEntity(Generator.generateAuthorUrl(), String[].class).getBody();
		} catch (Exception e) {
			log.info("无法访问规则地址：{} - {}", ruleLocation, e.getMessage());
			Generator.setRuleLocation(ruleLocationBackup);
			log.info("已切换到备用地址：{}", ruleLocationBackup);
		}
	}
}
