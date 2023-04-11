package com.lckp.jproxy.util;

import java.time.Duration;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.lckp.jproxy.constant.Token;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 检测工具类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-25
 */
@Slf4j
public class CheckUtil {
	CheckUtil() {
	}

	public static final int URL_APIKEY_OK = 2;

	public static final int ONLY_URL_OK = 1;

	public static final int URL_APIKEY_FAIL = 0;

	public static final Duration READ_TIMEOUT = Duration.ofSeconds(12);
	public static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(6);

	/**
	 * 
	 * 检查 Sonarr 或 Radarr 接口和密钥
	 *
	 * @param url
	 * @param apikey
	 * @return int
	 */
	public static int checkSonarrOrRadarrApi(String url, String apikey) {
		if (StringUtils.isBlank(url) && StringUtils.isBlank(apikey)) {
			return URL_APIKEY_FAIL;
		}
		try {
			RestTemplate restTemplate = new RestTemplateBuilder().setReadTimeout(READ_TIMEOUT)
					.setConnectTimeout(CONNECT_TIMEOUT).build();
			String body = restTemplate.getForEntity(url + "/api/v3/health?apikey=" + apikey, String.class)
					.getBody();
			if (body != null && body.contains("Unauthorized")) {
				log.error("Sonarr/Radarr Apikey 无效：{}", body);
				return ONLY_URL_OK;
			}
			return URL_APIKEY_OK;
		} catch (Exception e) {
			if (e.getMessage().contains("401")) {
				log.error("Sonarr/Radarr Apikey 无效：{}", e.getMessage());
				return ONLY_URL_OK;
			}
			log.error("Sonarr/Radarr URL 无效：{}", e.getMessage());
		}
		return URL_APIKEY_FAIL;
	}

	/**
	 * 
	 * 检查 Sonarr 索引器格式
	 *
	 * @param format
	 * @return boolean
	 */
	public static boolean checkSonarrIndexerFormat(String format) {
		if (StringUtils.isBlank(format)) {
			return false;
		}
		return format.contains("{" + Token.TITLE + "}") && format.contains("{" + Token.SEASON + "}")
				&& format.contains("{" + Token.EPISODE + "}");
	}

	/**
	 * 
	 * 检查 Sonarr 下载器格式
	 *
	 * @param format
	 * @return boolean
	 */
	public static boolean checkSonarrDownloaderFormat(String format) {
		if (StringUtils.isBlank(format)) {
			return false;
		}
		return format.contains("{" + Token.SEASON + "}") && format.contains("{" + Token.EPISODE + "}");
	}

	/**
	 * 
	 * 检查 Radarr 索引器格式
	 *
	 * @param format
	 * @return boolean
	 */
	public static boolean checkRadarrIndexerFormat(String format) {
		if (StringUtils.isBlank(format)) {
			return false;
		}
		return format.contains("{" + Token.TITLE + "}") && format.contains("{" + Token.YEAR + "}");
	}

	/**
	 * 
	 * 检查 Radarr 下载器格式
	 *
	 * @param format
	 * @return boolean
	 */
	public static boolean checkRadarrDownloaderFormat(String format) {
		if (StringUtils.isBlank(format)) {
			return false;
		}
		return format.contains("{" + Token.TITLE + "}");
	}

	/**
	 * 
	 * 检查 URL 是否有效
	 *
	 * @param url
	 * @return boolean
	 */
	public static boolean checkUrl(String url) {
		if (StringUtils.isBlank(url) || !url.startsWith("http")) {
			return false;
		}
		try {
			RestTemplate restTemplate = new RestTemplateBuilder().setReadTimeout(READ_TIMEOUT)
					.setConnectTimeout(CONNECT_TIMEOUT).build();
			restTemplate.getForEntity(url, String.class).getBody();
			return true;
		} catch (Exception e) {
			if (e.getMessage().contains("Too many follow-up requests")
					|| e.getMessage().contains("redirected too many")) {
				log.debug(e.getMessage());
				return true;
			}
			log.error("URL 无效：{}", e.getMessage());
		}
		return false;
	}

	/**
	 * 
	 * 检查 TMDB 接口和密钥
	 *
	 * @param url
	 * @param apikey
	 * @return int
	 */
	public static int checkTmdbApi(String url, String apikey) {
		if (StringUtils.isBlank(url) && StringUtils.isBlank(apikey)) {
			return URL_APIKEY_FAIL;
		}
		try {
			RestTemplate restTemplate = new RestTemplateBuilder().setReadTimeout(READ_TIMEOUT)
					.setConnectTimeout(CONNECT_TIMEOUT).build();
			String body = restTemplate.getForEntity(url + "/3/movie/550?api_key=" + apikey, String.class)
					.getBody();
			if (body != null && body.contains("Invalid API key")) {
				log.error("TMDB Apikey 无效：{}", body);
				return ONLY_URL_OK;
			}
			return URL_APIKEY_OK;
		} catch (Exception e) {
			if (e.getMessage().contains("401")) {
				log.error("TMDB Apikey 无效：{}", e.getMessage());
				return ONLY_URL_OK;
			}
			log.error("TMDB URL 无效：{}", e.getMessage());
		}
		return URL_APIKEY_FAIL;
	}

	/**
	 * 
	 * 检查正则表达式
	 *
	 * @param regex
	 * @return boolean
	 */
	public static boolean checkRegex(String regex) {
		if (StringUtils.isBlank(regex)) {
			return false;
		}
		try {
			Pattern.compile(regex);
			return true;
		} catch (Exception e) {
			log.error("正则表达式错误：{}", e.getMessage());
		}
		return false;
	}
}
