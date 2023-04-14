package com.lckp.jproxy.constant;

/**
 * <p>
 * 系统配置 Key
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
public class SystemConfigKey {
	SystemConfigKey() {
	}

	// Sonarr
	public static final String SONARR_URL = "sonarrUrl";
	public static final String SONARR_APIKEY = "sonarrApikey";
	public static final String SONARR_INDEXER_FORMAT = "sonarrIndexerFormat";
	public static final String SONARR_LANGUAGE_1 = "sonarrLanguage1";
	public static final String SONARR_LANGUAGE_2 = "sonarrLanguage2";
	// Radarr
	public static final String RADARR_URL = "radarrUrl";
	public static final String RADARR_APIKEY = "radarrApikey";
	public static final String RADARR_INDEXER_FORMAT = "radarrIndexerFormat";
	// 索引器
	public static final String JACKETT_URL = "jackettUrl";
	public static final String PROWLARR_URL = "prowlarrUrl";
	// 下载器
	public static final String QBITTORRENT_URL = "qbittorrentUrl";
	public static final String QBITTORRENT_USERNAME = "qbittorrentUsername";
	public static final String QBITTORRENT_PASSWORD = "qbittorrentPassword";
	public static final String TRANSMISSION_URL = "transmissionUrl";
	public static final String TRANSMISSION_USERNAME = "transmissionUsername";
	public static final String TRANSMISSION_PASSWORD = "transmissionPassword";
	// TMDB
	public static final String TMDB_URL = "tmdbUrl";
	public static final String TMDB_APIKEY = "tmdbApikey";
	// 净标题排除字符正则
	public static final String CLEAN_TITLE_REGEX = "cleanTitleRegex";
	// 规则同步作者
	public static final String RULE_SYNC_AUTHORS = "ruleSyncAuthors";
}
