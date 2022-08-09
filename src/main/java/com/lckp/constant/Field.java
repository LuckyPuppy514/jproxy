/**
 * @Title: Field.java
 * @version V1.0
 */
package com.lckp.constant;

/**
 * @className: Field
 * @description: 字段常量
 * @date 2022年7月18日
 * @author LuckyPuppy514
 */
public class Field {
	// 用户名字段名
	public static final String USERNAME = "username";
	// 标题
	public static final String TITLE = "title";

	// Jackett / Prowlarr 接口查询关键字字段
	public static final String SEARCH_KEY = "q";
	// Jackett / Prowlarr 接口查询类型字段
	public static final String SEARCH_TYPE = "t";
	// Jackett / Prowlarr 接口季字段
	public static final String SEASON = "season";
	// Jackett / Prowlarr 接口集字段
	public static final String EP = "ep";
	
	// Jackett/Prowlarr返回结果相关字段
	public static final String RESP_CHANNEL = "channel";
	public static final String RESP_ITEM = "item";
	public static final String RESP_TITLE = "title";
}
