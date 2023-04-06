package com.lckp.jproxy.model.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Sonarr 标题查询入参
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-25
 */
@Getter
@Setter
@Schema(description = "Sonarr 标题查询入参")
public class SonarrTitleQueryRequest extends PageRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "标题")
	private String title;

	@Schema(description = "TVDB 编号")
	private Integer tvdbId;
}
