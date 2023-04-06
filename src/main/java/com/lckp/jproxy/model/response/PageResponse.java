package com.lckp.jproxy.model.response;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * 分页查询返参
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-04
 */
@Getter
@Setter
@Schema(description = "分页查询返参")
public class PageResponse<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "页码")
	private long current = 1;

	@Schema(description = "页长")
	private long pageSize = 10;

	@Schema(description = "总数")
	private long total = 0;

	@Schema(description = "数据列表")
	private List<T> list = Collections.emptyList();

	public PageResponse() {
	}

	public PageResponse(IPage<T> mybatisPlusPage) {
		this.current = mybatisPlusPage.getCurrent();
		this.pageSize = mybatisPlusPage.getSize();
		this.total = mybatisPlusPage.getTotal();
		this.list = mybatisPlusPage.getRecords();
	}
}
