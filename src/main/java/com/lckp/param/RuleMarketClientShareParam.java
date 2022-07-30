/**
 * @Title: RuleMarketClientShareParam.java
 * @version V1.0
 */
package com.lckp.param;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: RuleMarketClientShareParam
 * @description: 规则市场客户端分享入参
 * @date 2022年7月27日
 * @author LuckyPuppy514
 */
@ApiModel("规则市场客户端分享入参")
public class RuleMarketClientShareParam {
	@ApiModelProperty(value = "规则 ID")
	public List<String> ruleIdList;

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
