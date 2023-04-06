package com.lckp.jproxy.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lckp.jproxy.entity.SystemConfig;

/**
 * <p>
 * SystemConfig 服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-12
 */
public interface ISystemConfigService extends IService<SystemConfig> {
	/**
	 * 
	 * 通过 key 获取对应配置的 value
	 *
	 * @param key
	 * @return String
	 */
	public String queryValueByKey(String key);

	/**
	 * 
	 * 更新系统配置
	 *
	 * @param systemConfigList
	 * @return boolean
	 */
	public boolean updateSystemConfig(List<SystemConfig> systemConfigList);
}
