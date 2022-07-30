/**
 * @Title: RuleTestExample.java
 * @version V1.0
 */
package com.lckp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: RuleTestExample
 * @description: 规则测试用例
 * @date 2022年7月15日
 * @author LuckyPuppy514
 */
@ApiModel("规则测试用例")
public class RuleTestExample {
	@ApiModelProperty(value = "规则测试用例 ID（主键）")
	private Integer exampleId;
	
	@ApiModelProperty(value = "用例内容")
	private String exampleContent;

	@ApiModelProperty("规则配置 ID（关联 RULE_CONFIG.RULE_ID）")
	private String ruleId;

	@ApiModelProperty(value = "有效状态（有效：1，无效：0，默认：1）")
	private Integer validstatus;
	
	@ApiModelProperty(value = "创建时间")
	private String createTime;
	
	@ApiModelProperty(value = "更新时间")
	private String updateTime;

	/**
	 * @return the exampleId
	 */
	public Integer getExampleId() {
		return exampleId;
	}

	/**
	 * @param exampleId the exampleId to set
	 */
	public void setExampleId(Integer exampleId) {
		this.exampleId = exampleId;
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
