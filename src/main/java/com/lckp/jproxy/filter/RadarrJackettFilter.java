package com.lckp.jproxy.filter;

import com.lckp.jproxy.service.IRadarrIndexerService;

/**
 * <p>
 * Radarr Jackett 过滤器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
public class RadarrJackettFilter extends RadarrIndexerFilter {

	/**
	 * @param radarrIndexerService
	 */
	public RadarrJackettFilter(IRadarrIndexerService radarrIndexerService) {
		super(radarrIndexerService);
	}

}
