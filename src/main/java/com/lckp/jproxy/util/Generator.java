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
	 * @param baseUrl
	 * @return String
	 */
	public static String generateAuthorUrl(String baseUrl) {
		return new StringBuilder(baseUrl).append("/author.json").toString();
	}

	/**
	 * 
	 * 生成 Sonarr 规则地址
	 *
	 * @param baseUrl
	 * @param author
	 * @return String
	 */
	public static String generateSonarrRuleUrl(String baseUrl, String author) {
		return new StringBuilder(baseUrl).append("/sonarr@").append(author.trim()).append(".json").toString();
	}

	/**
	 * 
	 * 生成 Radarr 规则地址
	 *
	 * @param baseUrl
	 * @param author
	 * @return String
	 */
	public static String generateRadarrRuleUrl(String baseUrl, String author) {
		return new StringBuilder(baseUrl).append("/radarr@").append(author.trim()).append(".json").toString();
	}
}
