package com.lckp.jproxy.controller;

import java.util.List;
import java.util.Locale;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.constant.Messages;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.entity.TmdbTitle;
import com.lckp.jproxy.model.request.TmdbTitleQueryRequest;
import com.lckp.jproxy.model.response.PageResponse;
import com.lckp.jproxy.service.ISonarrTitleService;
import com.lckp.jproxy.service.ISystemCacheService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.service.ITmdbTitleService;
import com.lckp.jproxy.util.FormatUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * TMDB 标题
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-26
 */
@Tag(name = "TMDB 标题")
@RequestMapping("/api/tmdb/title")
@RestController
@RequiredArgsConstructor
public class TmdbTitleController {

	private final ITmdbTitleService tmdbTitleService;

	private final ISonarrTitleService sonarrTitleService;

	private final ISystemConfigService systemConfigService;

	private final ISystemCacheService systemCacheService;

	private final MessageSource messageSource;

	@Operation(summary = "同步")
	@PostMapping("/sync")
	public ResponseEntity<String> sync(Locale locale) {
		try {
			if (tmdbTitleService.sync(sonarrTitleService.queryNeedSyncTmdbTitle())) {
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.badRequest()
						.body(messageSource.getMessage(Messages.TITLE_SYNC_TOO_OFTEN, null, locale));
			}
		} catch (Exception e) {
			systemCacheService.clear(CacheName.TMDB_TITLE_SYNC_INTERVAL);
			throw e;
		}
	}

	@Operation(summary = "查询")
	@GetMapping("/query")
	public ResponseEntity<PageResponse<TmdbTitle>> query(@ParameterObject TmdbTitleQueryRequest request) {
		PageResponse<TmdbTitle> response = new PageResponse<>(tmdbTitleService.query(request));
		String cleanTitleRegex = systemConfigService.queryValueByKey(SystemConfigKey.CLEAN_TITLE_REGEX);
		List<TmdbTitle> tmdbTitleList = response.getList();
		for (TmdbTitle tmdbTitle : tmdbTitleList) {
			tmdbTitle.setCleanTitle(FormatUtil.cleanTitle(tmdbTitle.getTitle(), cleanTitleRegex));
		}
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "删除")
	@PostMapping("/remove")
	public ResponseEntity<Void> remove(@RequestBody List<Integer> idList) {
		tmdbTitleService.removeBatchByIds(idList);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "保存")
	@PostMapping("/save")
	@CacheEvict(cacheNames = { CacheName.SONARR_SEARCH_TITLE, CacheName.INDEXER_SEARCH_OFFSET,
			CacheName.SONARR_RESULT_TITLE }, allEntries = true)
	public ResponseEntity<Void> save(@RequestBody TmdbTitle tmdbTitle) {
		tmdbTitleService.updateById(tmdbTitle);
		return ResponseEntity.ok().build();
	}
}
