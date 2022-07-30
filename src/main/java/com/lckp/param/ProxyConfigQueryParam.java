/**
 * @Title: ProxyConfigQueryParam.java
 * @version V1.0
 */
package com.lckp.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: ProxyConfigQueryParam
 * @description: 查询代理配置入参
 * @date 2022年7月15日
 * @author LuckyPuppy514
 */
@ApiModel("查询代理配置入参")
public class ProxyConfigQueryParam {
	@ApiModelProperty(value = "代理类型（Jackett/Prowlarr）", example = "Jackett", required = true)
	private String proxyType;

	/**
	 * @return the proxyType
	 */
	public String getProxyType() {
		return proxyType;
	}

	/**
	 * @param proxyType the proxyType to set
	 */
	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}
}
