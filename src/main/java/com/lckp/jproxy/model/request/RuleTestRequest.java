package com.lckp.jproxy.model.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 规则测试入参
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-29
 */
@Getter
@Setter
@Schema(description = "规则测试入参")
public class RuleTestRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "匹配正则")
	@NotBlank
	private String regex;

	@Schema(description = "替换内容")
	@NotBlank
	private String replacement;

	@Schema(description = "偏移量")
	private Integer offset = 0;

	@Schema(description = "例子")
	@NotBlank
	private String example;
}