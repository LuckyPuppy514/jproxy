/**
 * @Title: RuleMarketServerShareResp.java
 * @version V1.0
 */
package com.lckp.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: RuleMarketServerShareResp
 * @description: 分享返参
 * @date 2022年7月27日
 * @author LuckyPuppy514
 */
@ApiModel("分享返参")
public class RuleMarketServerShareResp {
	@ApiModelProperty(value = "规则 ID")
	private String ruleId;
	
	@ApiModelProperty(value = "分享 KEY")
	private String shareKey;

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
	
}
