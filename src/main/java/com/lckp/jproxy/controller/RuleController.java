package com.lckp.jproxy.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckp.jproxy.model.request.RuleTestRequest;
import com.lckp.jproxy.util.FormatUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 规则
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-29
 */
@Tag(name = "规则")
@RequestMapping("/api/rule")
@RestController
public class RuleController {

	@Operation(summary = "测试")
	@GetMapping("/test")
	public ResponseEntity<String> test(@Validated @ParameterObject RuleTestRequest request) {
		String[] examples = request.getExample().split("\\n");
		StringBuilder builder = new StringBuilder();
		for (String example : examples) {
			String value = example.replaceAll(request.getRegex(), request.getReplacement());
			value = FormatUtil.executeOffset(value, request.getOffset());
			builder.append(value + "\n");
		}
		return ResponseEntity.ok(builder.toString());
	}
}
