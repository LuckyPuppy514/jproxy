package com.lckp.jproxy.service.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.service.ISonarrJackettService;
import com.lckp.jproxy.service.ISonarrRuleService;
import com.lckp.jproxy.service.ISonarrTitleService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.service.ITmdbTitleService;

/**
 * <p>
 * Sonarr Jackett 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Service
public class SonarrJackettServiceImpl extends SonarrIndexerServiceImpl implements ISonarrJackettService {

	/**
	 * @param sonarrTitleService
	 * @param sonarrRuleService
	 * @param tmdbTitleService
	 * @param systemConfigService
	 */
	public SonarrJackettServiceImpl(ISonarrTitleService sonarrTitleService,
			ISonarrRuleService sonarrRuleService, ITmdbTitleService tmdbTitleService,
			ISystemConfigService systemConfigService) {
		super(sonarrTitleService, sonarrRuleService, tmdbTitleService, systemConfigService);
	}

	/**
	 * 
	 * @param path
	 * @return
	 * @see com.lckp.jproxy.service.impl.SonarrIndexerServiceImpl#getIndexerUrl(java.lang.String)
	 */
	@Override
	protected StringBuilder getIndexerUrl(String path) {
		StringBuilder url = new StringBuilder();
		url.append(systemConfigService.queryValueByKey(SystemConfigKey.JACKETT_URL));
		if (StringUtils.isNotBlank(path)) {
			url.append(path.replaceAll(Common.CHARON_JACKETT_PATH, ""));
		}
		return url;
	}

	/**
	 * 
	 * @return
	 * @see com.lckp.jproxy.service.impl.SonarrIndexerServiceImpl#getCharset()
	 */
	@Override
	protected Charset getCharset() {
		return StandardCharsets.UTF_8;
	}

}
