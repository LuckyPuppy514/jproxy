package com.lckp.jproxy.service.impl;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.service.ISystemCacheService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 系统缓存服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-26
 */
@Service
@RequiredArgsConstructor
public class SystemCacheServiceImpl implements ISystemCacheService {

	private final RedisTemplate<Object, Object> redisTemplate;

	/**
	 * @return
	 * @see com.lckp.jproxy.service.ISystemCacheService#clear()
	 */
	@Override
	@CacheEvict(cacheNames = "*", allEntries = true)
	public boolean clearAll() {
		redisTemplate.delete(CacheName.SONARR_TITLE_SYNC_INTERVAL);
		redisTemplate.delete(CacheName.TMDB_TITLE_SYNC_INTERVAL);
		redisTemplate.delete(CacheName.RADARR_TITLE_SYNC_INTERVAL);
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
		Set<Object> keys = redisTemplate.keys(cacheName + "*");
		for (Object key : keys) {
			redisTemplate.delete(key);
		}
		return true;
	}
}
