package com.lckp.jproxy.filter;

import com.lckp.jproxy.service.IIndexerService;

/**
 * <p>
 * Sonarr 索引器过滤器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
public abstract class SonarrIndexerFilter extends IndexerFilter {

	/**
	 * @param indexerService
	 */
	protected SonarrIndexerFilter(IIndexerService indexerService) {
		super(indexerService);
	}

}
