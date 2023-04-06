package com.lckp.jproxy.filter;

import com.lckp.jproxy.service.ISonarrIndexerService;

/**
 * <p>
 * Sonarr Prowlarr 过滤器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
public class SonarrProwlarrFilter extends SonarrIndexerFilter {

	/**
	 * @param sonarrIndexerService
	 */
	public SonarrProwlarrFilter(ISonarrIndexerService sonarrIndexerService) {
		super(sonarrIndexerService);
	}

}
