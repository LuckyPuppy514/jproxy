package com.lckp.jproxy.model.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 保存剧集范例入参
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-30
 */
@Getter
@Setter
@Schema(description = "保存剧集范例入参")
public class SonarrExampleSaveRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "原始文本")
	@NotBlank
	private String originalText;

}