package com.lckp.jproxy.service.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.service.ISonarrProwlarrService;
import com.lckp.jproxy.service.ISonarrRuleService;
import com.lckp.jproxy.service.ISonarrTitleService;
import com.lckp.jproxy.service.ISystemCacheService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.service.ITmdbTitleService;

/**
 * <p>
 * Sonarr Prowlarr 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Service
public class SonarrProwlarrServiceImpl extends SonarrIndexerServiceImpl implements ISonarrProwlarrService {

	/**
	 * @param sonarrTitleService
	 * @param sonarrRuleService
	 * @param tmdbTitleService
	 * @param systemConfigService
	 * @param systemCacheService
	 */
	public SonarrProwlarrServiceImpl(ISonarrTitleService sonarrTitleService,
			ISonarrRuleService sonarrRuleService, ITmdbTitleService tmdbTitleService,
			ISystemConfigService systemConfigService, ISystemCacheService systemCacheService) {
		super(sonarrTitleService, sonarrRuleService, tmdbTitleService, systemConfigService,
				systemCacheService);
	}

	@Override
	protected StringBuilder getIndexerUrl(String path) {
		StringBuilder url = new StringBuilder();
		url.append(systemConfigService.queryValueByKey(SystemConfigKey.PROWLARR_URL));
		if (StringUtils.isNotBlank(path)) {
			url.append(path.replaceAll(Common.CHARON_PROWLARR_PATH, ""));
		}
		return url;
	}

	@Override
	protected Charset getCharset() {
		return StandardCharsets.UTF_8;
	}
}
