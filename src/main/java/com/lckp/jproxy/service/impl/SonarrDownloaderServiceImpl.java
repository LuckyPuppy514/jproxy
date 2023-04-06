package com.lckp.jproxy.service.impl;

import org.springframework.stereotype.Service;

import com.lckp.jproxy.service.ISonarrDownloaderService;
import com.lckp.jproxy.service.ISonarrRuleService;
import com.lckp.jproxy.service.ISonarrTitleService;
import com.lckp.jproxy.service.ISystemConfigService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Sonarr 下载器服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Service
@RequiredArgsConstructor
public class SonarrDownloaderServiceImpl extends DownloaderServiceImpl implements ISonarrDownloaderService {
	protected final ISystemConfigService systemConfigService;

	protected final ISonarrTitleService sonarrTitleService;
	
	protected final ISonarrRuleService sonarrRuleService;
}
