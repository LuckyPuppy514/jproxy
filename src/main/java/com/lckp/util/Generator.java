/**
 * @Title: Generator.java
 * @version V1.0
 */
package com.lckp.util;

import java.util.UUID;

/**
 * @className: Generator
 * @description: 生成器
 * @date 2022年7月28日
 * @author LuckyPuppy514
 */
public class Generator {
	
	/**
	 * 
	 * @return
	 * @description: 生成规则 ID
	 */
	public static String createRuleId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 
	 * @return
	 * @description: 生成分享 KEY
	 */
	public static String createShareKey() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
