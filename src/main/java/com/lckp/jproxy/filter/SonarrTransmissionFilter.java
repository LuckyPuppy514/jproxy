package com.lckp.jproxy.filter;

import com.lckp.jproxy.service.IDownloaderService;

/**
 * <p>
 * Sonarr Transmission 过滤器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
public class SonarrTransmissionFilter extends DownloaderFilter {

	/**
	 * @param downloaderService
	 */
	public SonarrTransmissionFilter(IDownloaderService downloaderService) {
		super(downloaderService);
	}

}
