/**
 * @Title: ProxyConfigModifyParam.java
 * @version V1.0
 */
package com.lckp.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: ProxyConfigModifyParam
 * @description: 修改代理配置入参
 * @date 2022年7月19日
 * @author LuckyPuppy514
 */
@ApiModel("修改代理配置入参")
public class ProxyConfigModifyParam {
	@ApiModelProperty(value = "代理类型（Jackett/Prowlarr/qBittorrent）", example = "Jackett", required = true)
	private String proxyType;
	
	@ApiModelProperty(value = "代理地址", example = "127.0.0.1", required = true)
	private String proxyIp; 
	
	@ApiModelProperty(value = "代理端口", example = "9117", required = true)
	private String proxyPort; 
	
	@ApiModelProperty(value = "代理路径", example = "/api/**", required = true)
	private String proxyPath;

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

	/**
	 * @return the proxyIp
	 */
	public String getProxyIp() {
		return proxyIp;
	}

	/**
	 * @param proxyIp the proxyIp to set
	 */
	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	/**
	 * @return the proxyPort
	 */
	public String getProxyPort() {
		return proxyPort;
	}

	/**
	 * @param proxyPort the proxyPort to set
	 */
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	/**
	 * @return the proxyPath
	 */
	public String getProxyPath() {
		return proxyPath;
	}

	/**
	 * @param proxyPath the proxyPath to set
	 */
	public void setProxyPath(String proxyPath) {
		this.proxyPath = proxyPath;
	}
}
