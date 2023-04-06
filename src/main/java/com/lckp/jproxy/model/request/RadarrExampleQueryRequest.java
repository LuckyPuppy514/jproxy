package com.lckp.jproxy.model.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 电影范例查询入参
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-27
 */
@Getter
@Setter
@Schema(description = "电影范例查询入参")
public class RadarrExampleQueryRequest extends PageRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "原始文本")
	private String originalText;

	@Schema(description = "状态")
	private Integer validStatus;
}
