package com.lckp.jproxy.model.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Sonarr 规则查询入参
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-27
 */
@Getter
@Setter
@Schema(description = "Sonarr 规则查询入参")
public class SonarrRuleQueryRequest extends PageRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "标记")
	private String token;

	@Schema(description = "备注")
	private String remark;
}
