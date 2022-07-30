package com.lckp.mapper;

import com.lckp.model.ProxyConfig;
import com.lckp.param.ProxyConfigModifyParam;
import com.lckp.param.ProxyConfigQueryParam;

/**
* @ClassName: IProxyConfigMapper
* @Description: 代理配置mapper
* @author LuckyPuppy514
* @date 2022-07-15 14:39:56
*
*/
public interface IProxyConfigMapper {
	// 查询代理配置
	ProxyConfig query(ProxyConfigQueryParam param);
	
	// 修改代理配置
	int modify(ProxyConfigModifyParam param);
}
