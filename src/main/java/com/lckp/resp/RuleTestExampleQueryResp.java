/**
 * @Title: RuleTestExampleQueryResp.java
 * @version V1.0
 */
package com.lckp.resp;

import com.lckp.config.JProxy;
import com.lckp.constant.RegularType;
import com.lckp.util.FormatUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: RuleTestExampleQueryResp
 * @description: 查询测试用例返参
 * @date 2022年7月21日
 * @author LuckyPuppy514
 */
@ApiModel("查询测试用例返参")
public class RuleTestExampleQueryResp {
	@ApiModelProperty(value = "测试用例 ID（主键）")
	private String exampleId;
	
	@ApiModelProperty(value = "规则配置 ID")
	private String ruleId;
	
	@ApiModelProperty(value = "规则名称")
	private String ruleName;
	
	@ApiModelProperty(value = "格式化前")
	private String beforeFormat;
	
	@ApiModelProperty(value = "格式化后")
	private String afterFormat;
	
	@ApiModelProperty(value = "格式化状态（成功：1，失败：0）")
	private String formatStatus;

	@ApiModelProperty("正则类型")
	private String regularType;
	
	/**
	 * @return the exampleId
	 */
	public String getExampleId() {
		return exampleId;
	}

	/**
	 * @param exampleId the exampleId to set
	 */
	public void setExampleId(String exampleId) {
		this.exampleId = exampleId;
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
	 * @return the beforeFormat
	 */
	public String getBeforeFormat() {
		return beforeFormat;
	}

	/**
	 * @param beforeFormat the beforeFormat to set
	 */
	public void setBeforeFormat(String beforeFormat) {
		this.beforeFormat = beforeFormat;
	}

	/**
	 * @return the afterFormat
	 */
	public String getAfterFormat() {
		if (formatStatus == null) {
			if (regularType != null && regularType.equals(RegularType.Search.toString())) {
				afterFormat = FormatUtil.format(afterFormat, JProxy.searchRuleList);
			} else {
				afterFormat = FormatUtil.format(afterFormat, JProxy.resultRuleList);
			}
			
			if (!afterFormat.equals(beforeFormat)) {
				formatStatus = "1";
			} else {
				formatStatus = "0";
			}
		}
		return afterFormat;
	}


	/**
	 * @return the formatStatus
	 */
	public String getFormatStatus() {
		if (formatStatus == null) {
			getAfterFormat();
		}
		return formatStatus;
	}

	/**
	 * @param formatStatus the formatStatus to set
	 */
	public void setFormatStatus(String formatStatus) {
		this.formatStatus = formatStatus;
	}

	/**
	 * @param afterFormat the afterFormat to set
	 */
	public void setAfterFormat(String afterFormat) {
		this.afterFormat = afterFormat;
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
}
