package com.lckp.jproxy.filter;

import com.lckp.jproxy.service.ISonarrIndexerService;

/**
 * <p>
 * Sonarr Jackett 过滤器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
public class SonarrJackettFilter extends SonarrIndexerFilter {

	/**
	 * @param sonarrIndexerService
	 */
	public SonarrJackettFilter(ISonarrIndexerService sonarrIndexerService) {
		super(sonarrIndexerService);
	}
}
