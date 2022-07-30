/**
 * @Title: RuleConfigAddParam.java
 * @version V1.0
 */
package com.lckp.param;

import org.apache.commons.lang3.StringUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: RuleConfigAddParam
 * @description: 新增规则配置入参
 * @date 2022年7月19日
 * @author LuckyPuppy514
 */
@ApiModel("新增规则配置入参")
public class RuleConfigAddParam {
	@ApiModelProperty(value = "规则配置 ID（主键）", hidden = true)
	private String ruleId;
	
	@ApiModelProperty(value = "规则名称", required = true, example = "幻樱字幕组")
	private String ruleName;

	@ApiModelProperty(value = "规则类型", required = true, example = "Anime")
	private String ruleType;

	@ApiModelProperty(value = "规则语言", required = true, example = "中文")
	private String ruleLanguage;

	@ApiModelProperty(value = "正则类型（查询：Search，结果：Result）", required = true, example = "Result")
	private String regularType;

	@ApiModelProperty(value = "匹配正则", required = true, example = "")
	private String regularMatch;

	@ApiModelProperty(value = "替换正则", required = true, example = "")
	private String regularReplace;

	@ApiModelProperty(value = "执行规则（总是执行：Always，执行一次：Once，默认：Once）", required = true, example = "Once")
	private String executeRule;

	@ApiModelProperty(value = "执行优先级（数字越小优先级越高，默认：1000）", required = true, example = "1000")
	private String executePriority;

	@ApiModelProperty(value = "备注", required = false, example = "【幻樱字幕组】专属规则")
	private String remark;

	@ApiModelProperty(value = "有效状态（有效：1，无效：0，默认：1）", required = true, example = "1")
	private String validstatus;
	
	@ApiModelProperty(value = "分享 KEY", required = false, hidden = true)
	private String shareKey;
	
	@ApiModelProperty(value = "下载次数", required = false, hidden = true)
	private Integer downloadCount;
	
	@ApiModelProperty(value = "用例内容", required = true, example = "【幻樱字幕组】【4月新番】【古见同学有交流障碍症 Komi-san wa, Komyushou Desu.】【14】【GB_MP4】【1280X720】")
	private String exampleContent;

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
	 * @return the exampleContent
	 */
	public String getExampleContent() {
		return exampleContent;
	}

	/**
	 * @param exampleContent the exampleContent to set
	 */
	public void setExampleContent(String exampleContent) {
		if (StringUtils.isNotBlank(exampleContent)) {
			exampleContent = exampleContent.replaceAll("\\\\n", "\n");
		}
		this.exampleContent = exampleContent;
	}

}