package com.lckp.jproxy.util;

import java.util.UUID;

/**
 * <p>
 * 生成器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-28
 */
public class Generator {
	Generator() {
	}

	private static String ruleLocation = "http://127.0.0.1:8117";

	/**
	 * 
	 * 设置规则地址
	 *
	 * @param location void
	 */
	public static void setRuleLocation(String location) {
		ruleLocation = location;
	}

	/**
	 * 
	 * 生成 UUID
	 *
	 * @return String
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	/**
	 * 
	 * 生成规则作者地址
	 *
	 * @return String
	 */
	public static String generateAuthorUrl() {
		return new StringBuilder(ruleLocation).append("/author.json").toString();
	}

	/**
	 * 
	 * 生成 Sonarr 规则地址
	 *
	 * @param author
	 * @return String
	 */
	public static String generateSonarrRuleUrl(String author) {
		return new StringBuilder(ruleLocation).append("/sonarr@").append(author.trim()).append(".json")
				.toString();
	}

	/**
	 * 
	 * 生成 Radarr 规则地址
	 *
	 * @param author
	 * @return String
	 */
	public static String generateRadarrRuleUrl(String author) {
		return new StringBuilder(ruleLocation).append("/radarr@").append(author.trim()).append(".json")
				.toString();
	}
}
