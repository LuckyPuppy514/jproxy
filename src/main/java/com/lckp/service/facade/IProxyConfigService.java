package com.lckp.service.facade;

import com.lckp.model.ProxyConfig;
import com.lckp.param.ProxyConfigModifyParam;
import com.lckp.param.ProxyConfigQueryParam;

/**
* @ClassName: IProxyConfigService
* @Description: 代理配置service
* @author LuckyPuppy514
* @date 2022-07-15 14:39:56
*
*/
public interface IProxyConfigService {
	// 查询代理配置
	ProxyConfig query(ProxyConfigQueryParam param);
	
	// 修改代理配置
	int modify(ProxyConfigModifyParam param);
}
