/**
 * @Title: Rule.java
 * @version V1.0
 */
package com.lckp.jproxy.model;

import java.util.List;

/**
 * @className: Rule
 * @description: 替换规则
 * @date 2022年6月10日
 * @author LuckyPuppy514
 */
public class Rule {
	/*
	 * 精确规则（匹配成功一个，则不进行后续匹配，包括公共规则）
	 */
	private List<String> accurate;
	
	/*
	 * 公共规则（匹配所有公共规则）
	 */
	private List<String> common;

	/**
	 * @return the common
	 */
	public List<String> getCommon() {
		return common;
	}

	/**
	 * @param common the common to set
	 */
	public void setCommon(List<String> common) {
		this.common = common;
	}

	/**
	 * @return the accurate
	 */
	public List<String> getAccurate() {
		return accurate;
	}

	/**
	 * @param accurate the accurate to set
	 */
	public void setAccurate(List<String> accurate) {
		this.accurate = accurate;
	}
	
}
