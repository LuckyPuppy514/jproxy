package com.lckp.jproxy.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;

/**
 * <p>
 * 缓存配置
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-04-10
 */
@Configuration
public class CacheConfig {

	@Value("${time.cache-expires}")
	private long cacheExpires;

	@Value("${time.sync-interval}")
	private long syncInterval;

	@Value("${time.indexer-result-cache-expires}")
	private long indexerResultCacheExpires;

	@Bean(name = "syncIntervalCache")
	Cache<String, Integer> syncIntervalCache() {
		return Caffeine.newBuilder().expireAfter(new Expiry<String, Object>() {
			public long expireAfterCreate(String key, Object graph, long currentTime) {
				return TimeUnit.MINUTES.toNanos(syncInterval);
			}

			public long expireAfterUpdate(String key, Object graph, long currentTime, long currentDuration) {
				return currentDuration;
			}

			public long expireAfterRead(String key, Object graph, long currentTime, long currentDuration) {
				return currentDuration;
			}
		}).initialCapacity(3).maximumSize(10).build();
	}

	@Bean(name = "indexerResultCache")
	Cache<String, String> indexerResultCache() {
		return Caffeine.newBuilder().expireAfter(new Expiry<String, Object>() {
			public long expireAfterCreate(String key, Object graph, long currentTime) {
				return TimeUnit.MINUTES.toNanos(indexerResultCacheExpires);
			}

			public long expireAfterUpdate(String key, Object graph, long currentTime, long currentDuration) {
				return currentDuration;
			}

			public long expireAfterRead(String key, Object graph, long currentTime, long currentDuration) {
				return currentDuration;
			}
		}).initialCapacity(100).maximumSize(1000).build();
	}

	@Bean
	CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager();
		cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterAccess(cacheExpires, TimeUnit.MINUTES)
				.initialCapacity(100).maximumSize(1000));
		return cacheManager;
	}
}
