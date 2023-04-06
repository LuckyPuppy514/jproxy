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
import com.lckp.jproxy.entity.RadarrTitle;
import com.lckp.jproxy.model.request.RadarrTitleQueryRequest;
import com.lckp.jproxy.model.response.PageResponse;
import com.lckp.jproxy.service.IRadarrTitleService;
import com.lckp.jproxy.service.ISystemCacheService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 电影标题
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-26
 */
@Tag(name = "电影标题")
@RequestMapping("/api/radarr/title")
@RestController
@RequiredArgsConstructor
public class RadarrTitleController {

	private final IRadarrTitleService radarrTitleService;

	private final ISystemCacheService systemCacheService;

	private final MessageSource messageSource;

	@Operation(summary = "同步")
	@PostMapping("/sync")
	public ResponseEntity<String> sync(Locale locale) {
		try {
			if (radarrTitleService.sync()) {
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.badRequest()
						.body(messageSource.getMessage(Messages.TITLE_SYNC_TOO_OFTEN, null, locale));
			}
		} catch (Exception e) {
			systemCacheService.clear(CacheName.RADARR_TITLE_SYNC_INTERVAL);
			throw e;
		}
	}

	@Operation(summary = "查询")
	@GetMapping("/query")
	public ResponseEntity<PageResponse<RadarrTitle>> query(@ParameterObject RadarrTitleQueryRequest request) {
		return ResponseEntity.ok(new PageResponse<>(radarrTitleService.query(request)));
	}

	@Operation(summary = "删除")
	@PostMapping("/remove")
	public ResponseEntity<Void> remove(@RequestBody List<Integer> idList) {
		radarrTitleService.removeBatchByIds(idList);
		return ResponseEntity.ok().build();
	}
}
