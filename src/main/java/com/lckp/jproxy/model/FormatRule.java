/**
 * @Title: FormatRule.java
 * @version V1.0
 */
package com.lckp.jproxy.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: FormatRule
 * @description: 格式化规则
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
@ApiModel("格式化规则")
public class FormatRule {
	@ApiModelProperty("查询规则")
	private List<Regular> search;
	
	@ApiModelProperty("通用规则")
	private List<Regular> common;

	@ApiModelProperty("精确规则")
	private List<Regular> accurate;

	/**
	 * @return the search
	 */
	public List<Regular> getSearch() {
		return search;
	}

	/**
	 * @param search the search to set
	 */
	public void setSearch(List<Regular> search) {
		this.search = search;
	}

	/**
	 * @return the common
	 */
	public List<Regular> getCommon() {
		return common;
	}

	/**
	 * @param common the common to set
	 */
	public void setCommon(List<Regular> common) {
		this.common = common;
	}

	/**
	 * @return the accurate
	 */
	public List<Regular> getAccurate() {
		return accurate;
	}

	/**
	 * @param accurate the accurate to set
	 */
	public void setAccurate(List<Regular> accurate) {
		this.accurate = accurate;
	}
}
