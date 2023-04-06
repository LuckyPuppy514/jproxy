package com.lckp.jproxy.filter;

import com.lckp.jproxy.service.IDownloaderService;

/**
 * <p>
 * Sonarr qBittorrent 过滤器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
public class SonarrQbittorrentFilter extends DownloaderFilter {

	/**
	 * @param downloaderService
	 */
	public SonarrQbittorrentFilter(IDownloaderService downloaderService) {
		super(downloaderService);
	}

}
