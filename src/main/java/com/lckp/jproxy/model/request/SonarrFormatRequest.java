package com.lckp.jproxy.model.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Sonarr 格式化入参
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-18
 */
@Getter
@Setter
@Schema(description = "格式化入参")
public class SonarrFormatRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "文本", example = "[ANi] By the Grace of the Gods - 众神眷顾的男人 2 - 10 [1080P][Baha][WEB-DL][AAC AVC][CHT][MP4]")
	@NotBlank
	private String text;

	@Schema(description = "格式", example = "{title} {season}{episode} {language}{resolution}")
	@NotBlank
	private String format;
}
