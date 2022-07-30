/**
 * @Title: RuleConfig.java
 * @version V1.0
 */
package com.lckp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: RuleConfig
 * @description: 规则配置
 * @date 2022年7月18日
 * @author LuckyPuppy514
 */
@ApiModel("规则配置")
public class RuleConfig {
	@ApiModelProperty(value = "规则配置 ID（主键）")
	private String ruleId;
	
	@ApiModelProperty(value = "规则名称")
	private String ruleName;
	
	@ApiModelProperty(value = "规则类型")
	private String ruleType;
	
	@ApiModelProperty(value = "规则语言")
	private String ruleLanguage;
	
	@ApiModelProperty(value = "正则类型（查询：Search，结果：Result）")
	private String regularType;
	
	@ApiModelProperty(value = "匹配正则")
	private String regularMatch;
	
	@ApiModelProperty(value = "替换正则")
	private String regularReplace;
	
	@ApiModelProperty(value = "执行规则（总是执行：Always，执行一次：Once，默认：Once）")
	private String executeRule;
	
	@ApiModelProperty("执行优先级（数字越小优先级越高，默认：1000）")
	private String executePriority;
	
	@ApiModelProperty(value = "备注")
	private String remark;
	
	@ApiModelProperty(value = "有效状态（有效：1，无效：0，默认：1）")
	private String validstatus;
	
	@ApiModelProperty(value = "分享 KEY")
	private String shareKey;
	
	@ApiModelProperty(value = "下载次数")
	private Integer downloadCount;
	
	@ApiModelProperty(value = "下载状态（已下载：1，未下载：0，已上传：2）")
	private Integer downloadStatus;
	
	@ApiModelProperty(value = "创建时间")
	private String createTime;
	
	@ApiModelProperty(value = "更新时间")
	private String updateTime;

	/**
	 * @return the ruleId
	 */
	public String getRuleId() {
		return ruleId;
	}

	/**
	 * @param ruleId the ruleId to set
	 */
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
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
	 * @return the regularMatch
	 */
	public String getRegularMatch() {
		return regularMatch;
	}

	/**
	 * @param regularMatch the regularMatch to set
	 */
	public void setRegularMatch(String regularMatch) {
		this.regularMatch = regularMatch;
	}

	/**
	 * @return the regularReplace
	 */
	public String getRegularReplace() {
		return regularReplace;
	}

	/**
	 * @param regularReplace the regularReplace to set
	 */
	public void setRegularReplace(String regularReplace) {
		this.regularReplace = regularReplace;
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
	 * @return the executePriority
	 */
	public String getExecutePriority() {
		return executePriority;
	}

	/**
	 * @param executePriority the executePriority to set
	 */
	public void setExecutePriority(String executePriority) {
		this.executePriority = executePriority;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the validstatus
	 */
	public String getValidstatus() {
		return validstatus;
	}

	/**
	 * @param validstatus the validstatus to set
	 */
	public void setValidstatus(String validstatus) {
		this.validstatus = validstatus;
	}

	/**
	 * @return the shareKey
	 */
	public String getShareKey() {
		return shareKey;
	}

	/**
	 * @param shareKey the shareKey to set
	 */
	public void setShareKey(String shareKey) {
		this.shareKey = shareKey;
	}

	/**
	 * @return the downloadCount
	 */
	public Integer getDownloadCount() {
		return downloadCount;
	}

	/**
	 * @param downloadCount the downloadCount to set
	 */
	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
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
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
