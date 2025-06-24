package com.lckp.jproxy.constant;

/**
 * <p>
 * 通用常量
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
public class Common {
	Common() {
	}

	// 登陆拦截器路径
	public static final String INTERCEPTOR_ALL_PATH = "/**";
	public static final String INTERCEPTOR_SONARR_API_PATH = "/sonarr/**";
	public static final String INTERCEPTOR_RADARR_API_PATH = "/radarr/**";
	public static final String INTERCEPTOR_LOGIN_API_PATH = "/api/system/user/login";
	public static final String INTERCEPTOR_LOGIN_PAGE_PATH = "/login";
	public static final String INTERCEPTOR_INDEX_PAGE_PATH = "/index.html";
	public static final String INTERCEPTOR_STATIC_PATH = "/assets/**";
	public static final String[] INTERCEPTOR_KNFIE4J_PATHS = { "/doc.html", "/webjars/**",
			"/v3/api-docs/**" };
	public static final String[] INTERCEPTOR_OTHER_PATHS = { "/error", "/*.json", "/", "/favicon.ico",
			"/system/**", "/api/system/user/isLoginEnabled" };

	// Charon
	public static final String CHARON_ALL_PATH = "/.*";
	public static final String CHARON_IN_PATH = "/(?<path>.*)";
	public static final String CHARON_OUT_PATH = "/<path>";
	public static final String CHARON_JACKETT_PATH = "/(sonarr|radarr)/jackett";
	public static final String CHARON_PROWLARR_PATH = "/(sonarr|radarr)/prowlarr";
	public static final String CHARON_QBITTORRENT_PATH = "/(sonarr|radarr)/qbittorrent";
	public static final String CHARON_TRANSMISSION_PATH = "/(sonarr|radarr)/transmission";
	// Filter
	public static final String FILTER_SONARR_JACKETT_PATH = "/sonarr/jackett/*";
	public static final String FILTER_SONARR_PROWLARR_PATH = "/sonarr/prowlarr/*";
	public static final String FILTER_RADARR_JACKETT_PATH = "/radarr/jackett/*";
	public static final String FILTER_RADARR_PROWLARR_PATH = "/radarr/prowlarr/*";
	public static final String FILTER_SONARR_QBITTORRENT_PATH = "/sonarr/qbittorrent/api/v2/torrents/info";
	public static final String FILTER_SONARR_TRANSMISSION_PATH = "/sonarr/transmission/*";
	// 批量保存大小
	public static final int BATCH_SIZE = 200;
	// 规则同步作者
	public static final String RULE_SYNC_AUTHORS_ALL = "ALL";
	// 标题主规则 ID
	public static final String MOST_IMPORTANT_TITLE_RULE_ID = "00000000000000000000000000000000";
	// 视频文件扩展名正则表达式
	public static final String VIDEO_AND_SUBTITLE_EXTENSION_REGEX = "(\\.(mp4|avi|wmv|flv|mov|mkv|webm|mpg|mpeg|3gp|iso|ts|ass|srt|ssa|idx|sub))$";
	// 字幕文件扩展名正则表达式
	public static final String SUBTITLE_EXTENSION_REGEX = "(\\.(ass|srt|ssa|idx|sub))$";
	// 时间格式
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
}
