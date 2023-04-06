package com.lckp.jproxy.model.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Radarr 标题查询入参
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-25
 */
@Getter
@Setter
@Schema(description = "Radarr 标题查询入参")
public class RadarrTitleQueryRequest extends PageRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "标题")
	private String title;

	@Schema(description = "TMDB ID")
	private Integer tmdbId;
}
