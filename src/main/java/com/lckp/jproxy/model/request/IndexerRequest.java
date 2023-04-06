package com.lckp.jproxy.model.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 索引器请求参数
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-12
 */
@Getter
@Setter
@Schema(description = "索引器请求参数")
public class IndexerRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "查询关键字")
	private String searchKey;

	@Schema(description = "查询类型")
	private String searchType;

	@Schema(description = "季数")
	private String seasonNumber;

	@Schema(description = "集数")
	private String episodeNumber;

	@Schema(description = "偏移量")
	private Integer offset;

	@Schema(description = "数量限制")
	private Integer limit;
}
