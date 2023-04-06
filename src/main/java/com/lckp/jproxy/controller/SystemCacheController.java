package com.lckp.jproxy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckp.jproxy.model.request.SystemCacheClearRequest;
import com.lckp.jproxy.service.ISystemCacheService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 系统缓存
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-26
 */
@Tag(name = "系统缓存")
@RequestMapping("/api/system/cache")
@RestController
@RequiredArgsConstructor
public class SystemCacheController {

	private final ISystemCacheService systemCacheService;

	@Operation(summary = "清除所有")
	@PostMapping("/clearAll")
	public ResponseEntity<Void> clearAll() {
		systemCacheService.clearAll();
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "清除")
	@PostMapping("/clear")
	public ResponseEntity<Void> clear(@Validated @RequestBody SystemCacheClearRequest request) {
		systemCacheService.clear(request.getCacheName());
		return ResponseEntity.ok().build();
	}
}
