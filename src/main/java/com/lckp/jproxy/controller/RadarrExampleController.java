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

import com.lckp.jproxy.entity.RadarrExample;
import com.lckp.jproxy.model.request.RadarrExampleQueryRequest;
import com.lckp.jproxy.model.request.RadarrExampleSaveRequest;
import com.lckp.jproxy.model.response.PageResponse;
import com.lckp.jproxy.service.IRadarrExampleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import liquibase.util.MD5Util;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 电影范例
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-30
 */
@Tag(name = "电影范例")
@RequestMapping("/api/radarr/example")
@RestController
@RequiredArgsConstructor
public class RadarrExampleController {

	private final IRadarrExampleService radarrExampleService;

	@Operation(summary = "保存")
	@PostMapping("/save")
	public ResponseEntity<Void> save(@Validated @RequestBody RadarrExampleSaveRequest request) {
		String[] originalTexts = request.getOriginalText().split("\\n");
		List<RadarrExample> radarrExampleList = new ArrayList<>(originalTexts.length);
		for (String originalText : originalTexts) {
			RadarrExample example = new RadarrExample();
			example.setOriginalText(originalText);
			example.setHash(MD5Util.computeMD5(originalText).toUpperCase());
			radarrExampleList.add(example);
		}
		radarrExampleService.saveOrUpdateBatch(radarrExampleList);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "查询")
	@GetMapping("/query")
	public ResponseEntity<PageResponse<RadarrExample>> query(
			@ParameterObject RadarrExampleQueryRequest request, Locale locale) {
		return ResponseEntity.ok(new PageResponse<>(radarrExampleService.query(request, locale)));
	}

	@Operation(summary = "删除")
	@PostMapping("/remove")
	public ResponseEntity<Void> remove(@RequestBody List<String> idList) {
		radarrExampleService.removeBatchByIds(idList);
		return ResponseEntity.ok().build();
	}
}
