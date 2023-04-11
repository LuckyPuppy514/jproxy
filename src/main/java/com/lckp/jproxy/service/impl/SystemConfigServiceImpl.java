package com.lckp.jproxy.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.constant.Messages;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.constant.TableField;
import com.lckp.jproxy.constant.ValidStatus;
import com.lckp.jproxy.entity.SystemConfig;
import com.lckp.jproxy.exception.SystemConfigException;
import com.lckp.jproxy.mapper.SystemConfigMapper;
import com.lckp.jproxy.service.IQbittorrentService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.util.CheckUtil;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * SystemConfig 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-12
 */
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig>
		implements ISystemConfigService {

	private final IQbittorrentService qbittorrentService;

	private final ISystemConfigService proxy() {
		return (ISystemConfigService) AopContext.currentProxy();
	}

	/**
	 * @param systemConfigList
	 * @return
	 * @see com.lckp.jproxy.service.ISystemConfigService#updateSystemConfig(java.util.List)
	 */
	@Override
	@CacheEvict(cacheNames = { CacheName.SYSTEM_CONFIG }, allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public boolean updateSystemConfig(List<SystemConfig> systemConfigList) {
		Map<String, SystemConfig> map = new ConcurrentHashMap<>();
		for (SystemConfig systemConfig : systemConfigList) {
			map.put(systemConfig.getKey(), systemConfig);
		}
		// 检查 Sonarr 接口和密钥
		int result = CheckUtil.checkSonarrOrRadarrApi(map.get(SystemConfigKey.SONARR_URL).getValue(),
				map.get(SystemConfigKey.SONARR_APIKEY).getValue());
		map.get(SystemConfigKey.SONARR_URL).setValidStatus(ValidStatus.INVALID.getCode());
		map.get(SystemConfigKey.SONARR_APIKEY).setValidStatus(ValidStatus.INVALID.getCode());
		if (result == CheckUtil.ONLY_URL_OK) {
			map.get(SystemConfigKey.SONARR_URL).setValidStatus(ValidStatus.VALID.getCode());
			map.get(SystemConfigKey.SONARR_APIKEY).setValidStatus(ValidStatus.INVALID.getCode());
		} else if (result == CheckUtil.URL_APIKEY_OK) {
			map.get(SystemConfigKey.SONARR_URL).setValidStatus(ValidStatus.VALID.getCode());
			map.get(SystemConfigKey.SONARR_APIKEY).setValidStatus(ValidStatus.VALID.getCode());
		}
		// 检查 Sonarr 索引器和下载器格式
		SystemConfig sonarrIndexerFormat = map.get(SystemConfigKey.SONARR_INDEXER_FORMAT);
		sonarrIndexerFormat.setValidStatus(
				ValidStatus.getCode(CheckUtil.checkSonarrIndexerFormat(sonarrIndexerFormat.getValue())));
		SystemConfig sonarrDownloaderFormat = map.get(SystemConfigKey.SONARR_DOWNLOADER_FORMAT);
		sonarrDownloaderFormat.setValidStatus(ValidStatus
				.getCode(CheckUtil.checkSonarrDownloaderFormat(sonarrDownloaderFormat.getValue())));
		// 检查 Radarr 接口和密钥
		result = CheckUtil.checkSonarrOrRadarrApi(map.get(SystemConfigKey.RADARR_URL).getValue(),
				map.get(SystemConfigKey.RADARR_APIKEY).getValue());
		map.get(SystemConfigKey.RADARR_URL).setValidStatus(ValidStatus.INVALID.getCode());
		map.get(SystemConfigKey.RADARR_APIKEY).setValidStatus(ValidStatus.INVALID.getCode());
		if (result == CheckUtil.ONLY_URL_OK) {
			map.get(SystemConfigKey.RADARR_URL).setValidStatus(ValidStatus.VALID.getCode());
			map.get(SystemConfigKey.RADARR_APIKEY).setValidStatus(ValidStatus.INVALID.getCode());
		} else if (result == CheckUtil.URL_APIKEY_OK) {
			map.get(SystemConfigKey.RADARR_URL).setValidStatus(ValidStatus.VALID.getCode());
			map.get(SystemConfigKey.RADARR_APIKEY).setValidStatus(ValidStatus.VALID.getCode());
		}
		// 检查 Radarr 索引器格式
		SystemConfig radarrIndexerFormat = map.get(SystemConfigKey.RADARR_INDEXER_FORMAT);
		radarrIndexerFormat.setValidStatus(
				ValidStatus.getCode(CheckUtil.checkRadarrIndexerFormat(radarrIndexerFormat.getValue())));
		SystemConfig radarrDownloaderFormat = map.get(SystemConfigKey.RADARR_DOWNLOADER_FORMAT);
		radarrDownloaderFormat.setValidStatus(ValidStatus
				.getCode(CheckUtil.checkRadarrDownloaderFormat(radarrDownloaderFormat.getValue())));
		// 检查 qBittorrent
		SystemConfig qBittorrentUrl = map.get(SystemConfigKey.QBITTORRENT_URL);
		SystemConfig qBittorrentUsername = map.get(SystemConfigKey.QBITTORRENT_USERNAME);
		SystemConfig qBittorrentPassword = map.get(SystemConfigKey.QBITTORRENT_PASSWORD);
		qBittorrentUrl.setValidStatus(ValidStatus.getCode(CheckUtil.checkUrl(qBittorrentUrl.getValue())));
		qBittorrentUsername.setValidStatus(ValidStatus.INVALID.getCode());
		qBittorrentPassword.setValidStatus(ValidStatus.INVALID.getCode());
		if (ValidStatus.VALID.getCode().equals(qBittorrentUrl.getValidStatus())) {
			boolean isLogin = qbittorrentService.login(qBittorrentUrl.getValue(),
					qBittorrentUsername.getValue(), qBittorrentPassword.getValue());
			qBittorrentUsername.setValidStatus(ValidStatus.getCode(isLogin));
			qBittorrentPassword.setValidStatus(ValidStatus.getCode(isLogin));
		}
		// 检查 Jackett 地址
		SystemConfig jackett = map.get(SystemConfigKey.JACKETT_URL);
		jackett.setValidStatus(ValidStatus.getCode(CheckUtil.checkUrl(jackett.getValue())));
		// 检查 Prowlarr 地址
		SystemConfig prowlarr = map.get(SystemConfigKey.PROWLARR_URL);
		prowlarr.setValidStatus(ValidStatus.getCode(CheckUtil.checkUrl(prowlarr.getValue())));
		// 检查 TMDB 接口和密钥
		result = CheckUtil.checkTmdbApi(map.get(SystemConfigKey.TMDB_URL).getValue(),
				map.get(SystemConfigKey.TMDB_APIKEY).getValue());
		map.get(SystemConfigKey.TMDB_URL).setValidStatus(ValidStatus.INVALID.getCode());
		map.get(SystemConfigKey.TMDB_APIKEY).setValidStatus(ValidStatus.INVALID.getCode());
		if (result == CheckUtil.ONLY_URL_OK) {
			map.get(SystemConfigKey.TMDB_URL).setValidStatus(ValidStatus.VALID.getCode());
			map.get(SystemConfigKey.TMDB_APIKEY).setValidStatus(ValidStatus.INVALID.getCode());
		} else if (result == CheckUtil.URL_APIKEY_OK) {
			map.get(SystemConfigKey.TMDB_URL).setValidStatus(ValidStatus.VALID.getCode());
			map.get(SystemConfigKey.TMDB_APIKEY).setValidStatus(ValidStatus.VALID.getCode());
		}
		// 检查净标题处理规则
		SystemConfig cleanTitleRegex = map.get(SystemConfigKey.CLEAN_TITLE_REGEX);
		cleanTitleRegex.setValidStatus(ValidStatus.getCode(CheckUtil.checkRegex(cleanTitleRegex.getValue())));
		return proxy().saveOrUpdateBatch(systemConfigList);
	}

	/**
	 * @param key
	 * @return
	 * @see com.lckp.jproxy.service.ISystemConfigService#getValueByKey(java.lang.String)
	 */
	@Override
	@Cacheable(cacheNames = CacheName.SYSTEM_CONFIG)
	public String queryValueByKey(String key) {
		SystemConfig config = proxy().query().eq(TableField.KEY, key).one();
		if (ValidStatus.INVALID.getCode().equals(config.getValidStatus())) {
			throw new SystemConfigException(Messages.SYSTEM_CONFIG_INVALID_PREFIX + key);
		}
		return config.getValue();
	}

}
