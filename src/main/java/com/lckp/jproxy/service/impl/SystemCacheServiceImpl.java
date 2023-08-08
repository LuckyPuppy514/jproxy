package com.lckp.jproxy.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.service.ISystemCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 系统缓存服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemCacheServiceImpl implements ISystemCacheService {

	private final CacheManager cacheManager;

	@Autowired
	@Qualifier("syncIntervalCache")
	private Cache<String, Integer> syncIntervalCache;

	@Autowired
	@Qualifier("indexerResultCache")
	private Cache<String, String> indexerResultCache;

	/**
	 * @return
	 * @see com.lckp.jproxy.service.ISystemCacheService#clear()
	 */
	@Override
	public boolean clearAll() {
		cacheManager.getCacheNames().forEach(cacheName -> {
			cacheManager.getCache(cacheName).clear();
			log.debug("缓存已删除：{}", cacheName);
		});
		syncIntervalCache.asMap().clear();
		indexerResultCache.asMap().clear();
		return true;
	}

	/**
	 * 
	 * @param cacheName
	 * @return
	 * @see com.lckp.jproxy.service.ISystemCacheService#clear(java.lang.String)
	 */
	@Override
	public boolean clear(String cacheName) {
		if (StringUtils.isBlank(cacheName)) {
			return false;
		}
		org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			cache.clear();
		} else if (CacheName.INDEXER_RESULT.equals(cacheName)) {
			indexerResultCache.asMap().clear();
		} else {
			syncIntervalCache.asMap().remove(cacheName);
		}
		log.debug("缓存已删除：{}", cacheName);
		return true;
	}
}
