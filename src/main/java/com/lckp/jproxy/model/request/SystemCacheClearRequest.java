package com.lckp.jproxy.model.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 清除系统缓存入参
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-27
 */
@Getter
@Setter
@Schema(description = "清除系统缓存入参")
public class SystemCacheClearRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "缓存名称", example = "sonarr_rule")
	@NotBlank
	private String cacheName;
}
