/**
 * @Title: RuleTestExampleBatchParam.java
 * @version V1.0
 */
package com.lckp.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: RuleTestExampleBatchParam
 * @description: 规则测试用例批量操作入参
 * @date 2022年7月21日
 * @author LuckyPuppy514
 */
@ApiModel("规则测试用例批量操作入参")
public class RuleTestExampleBatchParam {
	@ApiModelProperty(value = "用例 ID")
	private String exampleId;

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
}
