package com.lckp.service.spring;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lckp.mapper.IProxyConfigMapper;
import com.lckp.model.ProxyConfig;
import com.lckp.param.ProxyConfigModifyParam;
import com.lckp.param.ProxyConfigQueryParam;
import com.lckp.service.facade.IProxyConfigService;

/**
* @ClassName: ProxyConfigServiceImpl
* @Description: 代理配置serviceImpl
* @author LuckyPuppy514
* @date 2022-07-15 14:39:56
*
*/
@Service("proxyConfigService")
public class ProxyConfigServiceImpl implements IProxyConfigService {
	
	@Autowired
	private IProxyConfigMapper proxyConfigMapper;

	/**
	 * 查询代理配置
	 */
	@Override
	public ProxyConfig query(ProxyConfigQueryParam param) {
		return proxyConfigMapper.query(param);
	}

	/**
	 * 修改代理配置
	 */
	@Override
	public int modify(ProxyConfigModifyParam param) {
		return proxyConfigMapper.modify(param);
	}
}
