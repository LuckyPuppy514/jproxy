package com.lckp.jproxy.filter;

import com.lckp.jproxy.service.IIndexerService;

/**
 * <p>
 * Radarr 索引器过滤器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
public abstract class RadarrIndexerFilter extends IndexerFilter {

	/**
	 * @param indexerService
	 */
	protected RadarrIndexerFilter(IIndexerService indexerService) {
		super(indexerService);
	}

}
