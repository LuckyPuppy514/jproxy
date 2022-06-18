/**
 * @Title: Regular.java
 * @version V1.0
 */
package com.lckp.jproxy.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: Regular
 * @description: 正则
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
@ApiModel("正则")
public class Regular {
	@ApiModelProperty(required = true, value = "匹配正则")	
	private String match;
	
	@ApiModelProperty(required = true, value = "替换正则")	
	private String replace;

	/**
	 * @return the match
	 */
	public String getMatch() {
		return match;
	}

	/**
	 * @param match the match to set
	 */
	public void setMatch(String match) {
		this.match = match;
	}

	/**
	 * @return the replace
	 */
	public String getReplace() {
		return replace;
	}

	/**
	 * @param replace the replace to set
	 */
	public void setReplace(String replace) {
		this.replace = replace;
	}

}
