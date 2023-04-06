package com.lckp.jproxy.model.request;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * 分页查询入参
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-04
 */
@Getter
@Setter
@Schema(description = "分页查询入参")
public class PageRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "页码")
	protected long current = 1;

	@Schema(description = "页长")
	protected long pageSize = 10;

	/**
	 * 
	 * 获取 Mybatis-Plus 分页参数
	 *
	 * @param <T>
	 * @return Page<T>
	 */
	public <T> Page<T> mybatisPlusPage() {
		return new Page<T>().setCurrent(this.current).setSize(this.pageSize);
	}
}
