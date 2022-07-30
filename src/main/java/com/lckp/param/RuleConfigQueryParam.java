/**
 * @Title: RuleConfigQueryParam.java
 * @version V1.0
 */
package com.lckp.param;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: RuleConfigQueryParam
 * @description: 查询规则配置入参
 * @date 2022年7月19日
 * @author LuckyPuppy514
 */
@ApiModel("查询规则配置入参")
public class RuleConfigQueryParam {
	@ApiModelProperty(value = "页码，默认：1", example = "1")
	private Long pageIndex = 1L;
	
	@ApiModelProperty(value = "页长，默认：20", example = "20")
	private Long pageSize = 20L;
	
	@ApiModelProperty(value = "规则名称", example = "幻樱字幕组")
	private String ruleName;
	
	@ApiModelProperty(value = "规则语言", example = "中文")
	private String ruleLanguage;
	
	@ApiModelProperty(value = "规则类型", example = "Custom")
	private String ruleType;
	
	@ApiModelProperty(value = "正则类型（查询：Search，结果：Result）", example = "Result")
	private String regularType;
	
	@ApiModelProperty(value = "执行规则（总是执行：Always，执行一次：Once，默认：Once）", example = "Once")
	private String executeRule;
	
	@ApiModelProperty(value = "有效状态（有效：1，无效：0，默认：1）", example = "1")
	private Integer validstatus;
	
	@ApiModelProperty(value = "下载状态（已下载：1，未下载：0）")
	private Integer downloadStatus;
	
	@ApiModelProperty(value = "已下载的规则配置 ID列表", hidden = true)
	private List<String> ruleIdList;

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
	 * @return the ruleLanguage
	 */
	public String getRuleLanguage() {
		return ruleLanguage;
	}

	/**
	 * @param ruleLanguage the ruleLanguage to set
	 */
	public void setRuleLanguage(String ruleLanguage) {
		this.ruleLanguage = ruleLanguage;
	}

	/**
	 * @return the ruleType
	 */
	public String getRuleType() {
		return ruleType;
	}

	/**
	 * @param ruleType the ruleType to set
	 */
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	/**
	 * @return the regularType
	 */
	public String getRegularType() {
		return regularType;
	}

	/**
	 * @param regularType the regularType to set
	 */
	public void setRegularType(String regularType) {
		this.regularType = regularType;
	}

	/**
	 * @return the executeRule
	 */
	public String getExecuteRule() {
		return executeRule;
	}

	/**
	 * @param executeRule the executeRule to set
	 */
	public void setExecuteRule(String executeRule) {
		this.executeRule = executeRule;
	}

	/**
	 * @return the validstatus
	 */
	public Integer getValidstatus() {
		return validstatus;
	}

	/**
	 * @param validstatus the validstatus to set
	 */
	public void setValidstatus(Integer validstatus) {
		this.validstatus = validstatus;
	}

	/**
	 * @return the downloadStatus
	 */
	public Integer getDownloadStatus() {
		return downloadStatus;
	}

	/**
	 * @param downloadStatus the downloadStatus to set
	 */
	public void setDownloadStatus(Integer downloadStatus) {
		this.downloadStatus = downloadStatus;
	}

	/**
	 * @return the ruleIdList
	 */
	public List<String> getRuleIdList() {
		return ruleIdList;
	}

	/**
	 * @param ruleIdList the ruleIdList to set
	 */
	public void setRuleIdList(List<String> ruleIdList) {
		this.ruleIdList = ruleIdList;
	}
	
}