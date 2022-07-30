/**
 * @Title: ProxyConfigQueryParam.java
 * @version V1.0
 */
package com.lckp.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: ProxyConfigQueryParam
 * @description: 查询规则测试用例入参
 * @date 2022年7月15日
 * @author LuckyPuppy514
 */
@ApiModel("查询规则测试用例入参")
public class RuleTestExampleQueryParam {
	@ApiModelProperty(value = "页码，默认：1", example = "1")
	private Long pageIndex = 1L;
	
	@ApiModelProperty(value = "页长，默认：20", example = "20")
	private Long pageSize = 20L;
	
	@ApiModelProperty(value = "规则名称", example = "幻樱字幕组")
	private String ruleName;
	
	@ApiModelProperty(value = "用例内容", example = "名侦探柯南")
	private String exampleContent;
	
	@ApiModelProperty(value = "格式化状态（成功：1，失败：0）", example = "1")
	private String formatStatus;

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

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * @return the exampleContent
	 */
	public String getExampleContent() {
		return exampleContent;
	}

	/**
	 * @param exampleContent the exampleContent to set
	 */
	public void setExampleContent(String exampleContent) {
		this.exampleContent = exampleContent;
	}

	/**
	 * @return the formatStatus
	 */
	public String getFormatStatus() {
		return formatStatus;
	}

	/**
	 * @param formatStatus the formatStatus to set
	 */
	public void setFormatStatus(String formatStatus) {
		this.formatStatus = formatStatus;
	}
}
