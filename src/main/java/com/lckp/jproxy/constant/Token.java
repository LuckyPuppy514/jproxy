package com.lckp.jproxy.constant;

/**
 * <p>
 * 规则标记
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-16
 */
public class Token {
	Token() {
	}

	public static final String REGEX = "\\{([^}]+)";

	public static final String TITLE = "title";

	public static final String SEASON = "season";

	public static final String EPISODE = "episode";

	public static final String LANGUAGE = "language";

	public static final String RESOLUTION = "resolution";

	public static final String SOURCE = "source";

	public static final String GROUP = "group";

	public static final String CLEAN_TITLE = "cleanTitle";

	public static final String YEAR = "year";
}
