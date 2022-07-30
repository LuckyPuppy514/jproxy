/**
 * @Title: ProxyConfigQueryParam.java
 * @version V1.0
 */
package com.lckp.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: ProxyConfigQueryParam
 * @description: 查询格式化规则入参
 * @date 2022年7月15日
 * @author LuckyPuppy514
 */
@ApiModel("查询格式化规则入参")
public class FormatRuleQueryParam {
	@ApiModelProperty(value = "页码，默认：1", example = "1")
	private Long pageIndex = 1L;
	
	@ApiModelProperty(value = "页长，默认：20", example = "20")
	private Long pageSize = 20L;

	/**
	 * @return the pageIndex
	 */
	public Long getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(Long pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the pageSize
	 */
	public Long getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
