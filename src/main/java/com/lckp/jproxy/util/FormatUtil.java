package com.lckp.jproxy.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.lckp.jproxy.constant.Token;

/**
 * <p>
 * 格式化工具类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
public class FormatUtil {
	FormatUtil() {
	}

	// 分隔符
	private static final String REGEX_SEPARATOR = "[\\[\\]【】]";
	// 分隔符占位符
	public static final String PLACEHOLDER_SEPARATOR = "/";
	private static final String PLACEHOLDER_SEPARATORS = "/+";
	// 正则表达式特殊字符
	private static final String REGEX_SPECIAL_CHAR = "[\\$\\(\\)\\*\\+\\.\\?\\^\\{\\}\\|\\\\]";
	// 冠词
	private static final String REGEX_ARTICLE = "(\\b|\\s)((?i)a|an|the)\s";
	// 占位符
	public static final String PLACEHOLDER = "\s";
	private static final String PLACEHOLDERS = "\s+";

	/**
	 * 
	 * 清洗标题
	 *
	 * @param title
	 * @param regex
	 * @return String
	 */
	public static String cleanTitle(String title, String regex) {
		title = title.replaceAll(REGEX_SEPARATOR, PLACEHOLDER_SEPARATOR);
		title = title.replaceAll(REGEX_SPECIAL_CHAR, PLACEHOLDER);
		title = title.replaceAll(REGEX_ARTICLE, PLACEHOLDER);
		String cleanTitle = title.replaceAll(regex, PLACEHOLDER);
		if (StringUtils.isBlank(cleanTitle)) {
			cleanTitle = title;
		}
		cleanTitle = cleanTitle.replaceAll(PLACEHOLDER_SEPARATORS, PLACEHOLDER_SEPARATOR);
		cleanTitle = cleanTitle.replaceAll(PLACEHOLDERS, PLACEHOLDER);
		return cleanTitle.toLowerCase().trim();
	}

	/**
	 * 
	 * 去除标题中的年份
	 *
	 * @param title
	 * @return String
	 */
	public static String removeYear(String title) {
		if (StringUtils.isNotBlank(title)) {
			return title.replaceAll(" (19|20)\\d{2}$", "");
		}
		return "";
	}

	/**
	 * 
	 * 去除标题中的集数
	 *
	 * @param title
	 * @return String
	 */
	public static String removeEpisode(String title) {
		if (StringUtils.isNotBlank(title)) {
			return title.replaceAll(" 0*(\\d{1,3}|1[0-8]\\d{2})$", "");
		}
		return "";
	}

	/**
	 * 
	 * 去除标题中的季数
	 *
	 * @param title
	 * @return String
	 */
	public static String removeSeason(String title) {
		if (StringUtils.isNotBlank(title)) {
			return title.replaceAll(" (S\\d+)$", "");
		}
		return "";
	}

	/**
	 * 
	 * 去除标题中的季数和集数
	 *
	 * @param title
	 * @return String
	 */
	public static String removeSeasonEpisode(String title) {
		if (StringUtils.isNotBlank(title)) {
			return title.replaceAll(" (S\\d+ |)\\d+$", "");
		}
		return "";
	}

	/**
	 * 
	 * 用 value 替换 token
	 *
	 * @param token
	 * @param value
	 * @param text
	 * @param offset
	 * @return String
	 */
	public static String replaceToken(String token, String value, String text, Integer offset) {
		return replaceToken(token, executeOffset(value, offset), text);
	}

	/**
	 * 
	 * 执行偏移
	 *
	 * @param value
	 * @param offset
	 * @return String
	 */
	public static String executeOffset(String value, Integer offset) {
		if (offset != null && !Integer.valueOf(0).equals(offset)) {
			Matcher matcher = Pattern.compile("(\\d+)").matcher(value);
			while (matcher.find()) {
				String numberString = matcher.group(1);
				int number = Integer.parseInt(numberString);
				value = value.replace(numberString, String.valueOf(number + offset));
			}
		}
		return value;
	}

	/**
	 * 
	 * 用 value 替换 token
	 *
	 * @param token
	 * @param value
	 * @param text
	 * @return String
	 */
	public static String replaceToken(String token, String value, String text) {
		return text.replace("{" + token + "}", value);
	}

	/**
	 * 
	 * 移除 token
	 *
	 * @param token
	 * @param text
	 * @return String
	 */
	public static String removeToken(String token, String text) {
		return text.replace("{" + token + "}", "");
	}

	/**
	 * 
	 * 移除所有 token
	 *
	 * @param text
	 * @return String
	 */
	public static String removeAllToken(String text) {
		return text.replaceAll(Token.REGEX, "");
	}
}
