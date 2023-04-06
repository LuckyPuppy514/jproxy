package com.lckp.jproxy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckp.jproxy.entity.SonarrExample;
import com.lckp.jproxy.model.request.SonarrExampleQueryRequest;
import com.lckp.jproxy.model.request.SonarrExampleSaveRequest;
import com.lckp.jproxy.model.response.PageResponse;
import com.lckp.jproxy.service.ISonarrExampleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import liquibase.util.MD5Util;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 剧集范例
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-30
 */
@Tag(name = "剧集范例")
@RequestMapping("/api/sonarr/example")
@RestController
@RequiredArgsConstructor
public class SonarrExampleController {

	private final ISonarrExampleService sonarrExampleService;

	@Operation(summary = "保存")
	@PostMapping("/save")
	public ResponseEntity<Void> save(@Validated @RequestBody SonarrExampleSaveRequest request) {
		String[] originalTexts = request.getOriginalText().split("\\n");
		List<SonarrExample> sonarrExampleList = new ArrayList<>(originalTexts.length);
		for (String originalText : originalTexts) {
			SonarrExample example = new SonarrExample();
			example.setOriginalText(originalText);
			example.setHash(MD5Util.computeMD5(originalText).toUpperCase());
			sonarrExampleList.add(example);
		}
		sonarrExampleService.saveOrUpdateBatch(sonarrExampleList);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "查询")
	@GetMapping("/query")
	public ResponseEntity<PageResponse<SonarrExample>> query(
			@ParameterObject SonarrExampleQueryRequest request, Locale locale) {
		return ResponseEntity.ok(new PageResponse<>(sonarrExampleService.query(request, locale)));
	}

	@Operation(summary = "删除")
	@PostMapping("/remove")
	public ResponseEntity<Void> remove(@RequestBody List<String> idList) {
		sonarrExampleService.removeBatchByIds(idList);
		return ResponseEntity.ok().build();
	}
}
