package com.lckp.jproxy.service.impl;

import org.springframework.stereotype.Service;

import com.lckp.jproxy.service.ISonarrRuleService;
import com.lckp.jproxy.service.ISonarrTitleService;
import com.lckp.jproxy.service.ISonarrTransmissionService;
import com.lckp.jproxy.service.ISystemConfigService;

/**
 * <p>
 * Sonarr Transmission 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Service
public class SonarrTransmissionServiceImpl extends SonarrDownloaderServiceImpl
		implements ISonarrTransmissionService {

	/**
	 * @param systemConfigService
	 * @param sonarrTitleService
	 * @param sonarrRuleService
	 */
	public SonarrTransmissionServiceImpl(ISystemConfigService systemConfigService,
			ISonarrTitleService sonarrTitleService, ISonarrRuleService sonarrRuleService) {
		super(systemConfigService, sonarrTitleService, sonarrRuleService);
	}
}
