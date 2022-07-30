/**
 * @Title: ProxyConfig.java
 * @version V1.0
 */
package com.lckp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: ProxyConfig
 * @description: 代理配置
 * @date 2022年7月15日
 * @author LuckyPuppy514
 */
@ApiModel("代理配置")
public class ProxyConfig {
	@ApiModelProperty(value = "代理配置 ID（主键）")
	private Integer proxyId; 
	
	@ApiModelProperty(value = "代理类型（Jackett/Prowlarr）")
	private String proxyType;
	
	@ApiModelProperty(value = "代理地址")
	private String proxyIp; 
	
	@ApiModelProperty(value = "代理端口")
	private String proxyPort; 
	
	@ApiModelProperty(value = "代理路径")
	private String proxyPath; 
	
	@ApiModelProperty(value = "有效状态（有效：1，无效：0，默认：1）")
	private Integer validstatus;
	
	@ApiModelProperty(value = "创建时间")
	private String createTime;
	
	@ApiModelProperty(value = "更新时间")
	private String updateTime;

	/**
	 * @return the proxyId
	 */
	public Integer getProxyId() {
		return proxyId;
	}

	/**
	 * @param proxyId the proxyId to set
	 */
	public void setProxyId(Integer proxyId) {
		this.proxyId = proxyId;
	}

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

	/**
	 * @return the validstatus
	 */
	public Integer getValidstatus() {
		return validstatus;
	}

	/**
	 * @param validstatus the validstatus to set
	 */
	public void setValidstatus(Integer validstatus) {
		this.validstatus = validstatus;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}