/**
 * @Title: RuleConfigBatchParam.java
 * @version V1.0
 */
package com.lckp.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: RuleConfigBatchParam
 * @description: 规则配置批量操作入参
 * @date 2022年7月19日
 * @author LuckyPuppy514
 */
@ApiModel("规则配置批量操作入参")
public class RuleConfigBatchParam {
	@ApiModelProperty(value = "规则配置 ID", required = true)
	private String ruleId;
	
	@ApiModelProperty(value = "有效状态", required = true)
	private String validstatus;

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
	public String getValidstatus() {
		return validstatus;
	}

	/**
	 * @param validstatus the validstatus to set
	 */
	public void setValidstatus(String validstatus) {
		this.validstatus = validstatus;
	}
}