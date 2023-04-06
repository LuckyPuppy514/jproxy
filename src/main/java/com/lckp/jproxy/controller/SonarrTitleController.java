package com.lckp.jproxy.controller;

import java.util.List;
import java.util.Locale;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.constant.Messages;
import com.lckp.jproxy.entity.SonarrTitle;
import com.lckp.jproxy.model.request.SonarrTitleQueryRequest;
import com.lckp.jproxy.model.response.PageResponse;
import com.lckp.jproxy.service.ISonarrTitleService;
import com.lckp.jproxy.service.ISystemCacheService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 剧集标题
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-26
 */
@Tag(name = "剧集标题")
@RequestMapping("/api/sonarr/title")
@RestController
@RequiredArgsConstructor
public class SonarrTitleController {

	private final ISonarrTitleService sonarrTitleService;

	private final ISystemCacheService systemCacheService;

	private final MessageSource messageSource;

	@Operation(summary = "同步")
	@PostMapping("/sync")
	public ResponseEntity<String> sync(Locale locale) {
		try {
			if (sonarrTitleService.sync()) {
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.badRequest()
						.body(messageSource.getMessage(Messages.TITLE_SYNC_TOO_OFTEN, null, locale));
			}
		} catch (Exception e) {
			systemCacheService.clear(CacheName.SONARR_TITLE_SYNC_INTERVAL);
			throw e;
		}
	}

	@Operation(summary = "查询")
	@GetMapping("/query")
	public ResponseEntity<PageResponse<SonarrTitle>> query(@ParameterObject SonarrTitleQueryRequest request) {
		return ResponseEntity.ok(new PageResponse<>(sonarrTitleService.query(request)));
	}

	@Operation(summary = "删除")
	@PostMapping("/remove")
	public ResponseEntity<Void> remove(@RequestBody List<Integer> idList) {
		sonarrTitleService.removeBatchByIds(idList);
		return ResponseEntity.ok().build();
	}
}
