package com.lckp.jproxy.service;

/**
 * <p>
 * 系统缓存服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-26
 */
public interface ISystemCacheService {
	/**
	 * 
	 * 清除所有缓存
	 *
	 * @return boolean
	 */
	public boolean clearAll();

	/**
	 * 
	 * 清除指定缓存
	 *
	 * @param cacheName
	 * @return boolean
	 */
	public boolean clear(String cacheName);

}
