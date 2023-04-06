package com.lckp.jproxy.filter;

import com.lckp.jproxy.service.IRadarrIndexerService;

/**
 * <p>
 * Radarr Prowlarr 过滤器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
public class RadarrProwlarrFilter extends RadarrIndexerFilter {

	/**
	 * @param radarrIndexerService
	 */
	public RadarrProwlarrFilter(IRadarrIndexerService radarrIndexerService) {
		super(radarrIndexerService);
	}

}
