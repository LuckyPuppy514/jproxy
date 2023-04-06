package com.lckp.jproxy.service.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.service.IRadarrProwlarrService;
import com.lckp.jproxy.service.IRadarrRuleService;
import com.lckp.jproxy.service.IRadarrTitleService;
import com.lckp.jproxy.service.ISystemConfigService;

/**
 * <p>
 * Radarr Prowlarr 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Service
public class RadarrProwlarrServiceImpl extends RadarrIndexerServiceImpl implements IRadarrProwlarrService {

	/**
	 * @param radarrTitleService
	 * @param radarrRuleService
	 * @param systemConfigService
	 */
	public RadarrProwlarrServiceImpl(IRadarrTitleService radarrTitleService,
			IRadarrRuleService radarrRuleService, ISystemConfigService systemConfigService) {
		super(radarrTitleService, radarrRuleService, systemConfigService);
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
