/**
 * @Title: ProxyParam.java
 * @version V1.0
 */
package com.lckp.param;

import com.lckp.constant.ProxyType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: ProxyParam
 * @description: 代理参数
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
@ApiModel("代理参数")
public class ProxyParam {
	@ApiModelProperty("代理类型")
	private ProxyType proxyType;
	
	@ApiModelProperty(value = "代理地址")
	private String proxyUrl;
	
	@ApiModelProperty(value = "代理路径")
	private String proxyPath;
	
	@ApiModelProperty("查询关键字字段名")
	private String searchKeyField;
	
	@ApiModelProperty("查询关键字值")
	private String searchKeyValue;
	
	@ApiModelProperty("查询参数")
	private String paramString;

	/**
	 * @return the proxyType
	 */
	public ProxyType getProxyType() {
		return proxyType;
	}

	/**
	 * @param proxyType the proxyType to set
	 */
	public void setProxyType(ProxyType proxyType) {
		this.proxyType = proxyType;
	}

	/**
	 * @return the proxyUrl
	 */
	public String getProxyUrl() {
		return proxyUrl;
	}

	/**
	 * @param proxyUrl the proxyUrl to set
	 */
	public void setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
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
	 * @return the searchKeyField
	 */
	public String getSearchKeyField() {
		return searchKeyField;
	}

	/**
	 * @param searchKeyField the searchKeyField to set
	 */
	public void setSearchKeyField(String searchKeyField) {
		this.searchKeyField = searchKeyField;
	}

	/**
	 * @return the searchKeyValue
	 */
	public String getSearchKeyValue() {
		return searchKeyValue;
	}

	/**
	 * @param searchKeyValue the searchKeyValue to set
	 */
	public void setSearchKeyValue(String searchKeyValue) {
		this.searchKeyValue = searchKeyValue;
	}

	/**
	 * @return the paramString
	 */
	public String getParamString() {
		return paramString;
	}

	/**
	 * @param paramString the paramString to set
	 */
	public void setParamString(String paramString) {
		this.paramString = paramString;
	}

}