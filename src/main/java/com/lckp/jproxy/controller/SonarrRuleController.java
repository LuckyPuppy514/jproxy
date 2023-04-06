package com.lckp.jproxy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.constant.Messages;
import com.lckp.jproxy.constant.TableField;
import com.lckp.jproxy.constant.ValidStatus;
import com.lckp.jproxy.entity.SonarrRule;
import com.lckp.jproxy.model.request.SonarrRuleQueryRequest;
import com.lckp.jproxy.model.response.PageResponse;
import com.lckp.jproxy.service.ISonarrRuleService;
import com.lckp.jproxy.util.FileUtil;
import com.lckp.jproxy.util.Generator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 剧集规则
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-26
 */
@Slf4j
@Tag(name = "剧集规则")
@RequestMapping("/api/sonarr/rule")
@RestController
@RequiredArgsConstructor
public class SonarrRuleController {

	private final ISonarrRuleService sonarrRuleService;

	private final MessageSource messageSource;

	@Operation(summary = "同步")
	@PostMapping("/sync")
	public ResponseEntity<String> sync(Locale locale) {
		if (sonarrRuleService.sync()) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.badRequest()
					.body(messageSource.getMessage(Messages.TITLE_SYNC_TOO_OFTEN, null, locale));
		}
	}

	@Operation(summary = "查询")
	@GetMapping("/query")
	public ResponseEntity<PageResponse<SonarrRule>> query(@ParameterObject SonarrRuleQueryRequest request) {
		return ResponseEntity.ok(new PageResponse<>(sonarrRuleService.query(request)));
	}

	@Operation(summary = "保存")
	@PostMapping("/save")
	@CacheEvict(cacheNames = CacheName.SONARR_RULE, allEntries = true)
	public ResponseEntity<String> save(@Validated @RequestBody SonarrRule request, Locale locale) {
		try {
			Pattern.compile(request.getRegex()).matcher(request.getRegex())
					.replaceAll(request.getReplacement());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
		if (StringUtils.isBlank(request.getId())) {
			request.setId(Generator.generateUUID());
		} else if (Common.MOST_IMPORTANT_TITLE_RULE_ID.equals(request.getId())) {
			return ResponseEntity.badRequest()
					.body(messageSource.getMessage(Messages.RULE_MODIFY_PRIMARY_FORBIDDEN, null, locale));
		}
		sonarrRuleService.saveOrUpdate(request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "删除")
	@PostMapping("/remove")
	@CacheEvict(cacheNames = CacheName.SONARR_RULE, allEntries = true)
	public ResponseEntity<String> remove(@RequestBody List<String> idList, Locale locale) {
		for (String id : idList) {
			if (Common.MOST_IMPORTANT_TITLE_RULE_ID.equals(id)) {
				return ResponseEntity.badRequest()
						.body(messageSource.getMessage(Messages.RULE_MODIFY_PRIMARY_FORBIDDEN, null, locale));
			}
		}
		sonarrRuleService.removeBatchByIds(idList);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "启用")
	@PostMapping("/enable")
	public ResponseEntity<Void> enable(@RequestBody List<String> idList) {
		sonarrRuleService.switchValidStatus(idList, ValidStatus.VALID);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "禁用")
	@PostMapping("/disable")
	public ResponseEntity<String> disable(@RequestBody List<String> idList, Locale locale) {
		for (String id : idList) {
			if (Common.MOST_IMPORTANT_TITLE_RULE_ID.equals(id)) {
				return ResponseEntity.badRequest()
						.body(messageSource.getMessage(Messages.RULE_MODIFY_PRIMARY_FORBIDDEN, null, locale));
			}
		}
		sonarrRuleService.switchValidStatus(idList, ValidStatus.INVALID);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "导出")
	@PostMapping("/export")
	public ResponseEntity<List<SonarrRule>> export(@RequestBody List<String> idList) {
		if (idList == null || idList.isEmpty()) {
			return ResponseEntity.ok(sonarrRuleService.query()
					.select(TableField.ID, TableField.TOKEN, TableField.PRIORITY, TableField.REGEX,
							TableField.REPLACEMENT, TableField.OFFSET, TableField.EXAMPLE, TableField.REMARK,
							TableField.AUTHOR)
					.list());
		}
		return ResponseEntity.ok(sonarrRuleService.listByIds(idList));
	}

	@Operation(summary = "导入")
	@PostMapping("/import")
	@CacheEvict(cacheNames = CacheName.SONARR_RULE, allEntries = true)
	public ResponseEntity<String> importSonarrRule(MultipartFile file) {
		try {
			String content = FileUtil.read(file);
			if (content == null) {
				return ResponseEntity.badRequest().build();
			}
			List<SonarrRule> sonarrRuleList = JSON.parseObject(content,
					new TypeReference<List<SonarrRule>>() {
					});
			sonarrRuleService.saveOrUpdateBatch(sonarrRuleList);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("导入出错：", e);
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@Operation(summary = "查询 token 列表")
	@GetMapping("/token/list")
	public ResponseEntity<List<String>> listToken() {
		List<SonarrRule> sonarrRuleList = sonarrRuleService.query().select(TableField.TOKEN)
				.groupBy(TableField.TOKEN).list();
		List<String> tokenList = new ArrayList<>(sonarrRuleList.size());
		for (SonarrRule sonarrRule : sonarrRuleList) {
			tokenList.add(sonarrRule.getToken());
		}
		return ResponseEntity.ok(tokenList);
	}
}
