/**
 * @Title: ProxyParam.java
 * @version V1.0
 */
package com.lckp.jproxy.param;

import com.lckp.jproxy.constant.ProxyType;
import com.lckp.jproxy.constant.SeriesType;
import com.lckp.jproxy.model.FormatRule;

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
	
	@ApiModelProperty("接口地址")
	private String url;
	
	@ApiModelProperty("接口路径")
	private String path;
	
	@ApiModelProperty("查询关键字字段名")
	private String searchKeyField;
	
	@ApiModelProperty("查询关键字值")
	private String searchKeyValue;
	
	@ApiModelProperty("查询参数")
	private String paramString;
	
	@ApiModelProperty("系列类型")
	private SeriesType seriesType = SeriesType.ANIME;
	
	@ApiModelProperty("格式化规则")
	private FormatRule formatRule;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
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
	/**
	 * @return the seriesType
	 */
	public SeriesType getSeriesType() {
		return seriesType;
	}
	/**
	 * @param seriesType the seriesType to set
	 */
	public void setSeriesType(SeriesType seriesType) {
		this.seriesType = seriesType;
	}
	/**
	 * @return the formatRule
	 */
	public FormatRule getFormatRule() {
		return formatRule;
	}
	/**
	 * @param formatRule the formatRule to set
	 */
	public void setFormatRule(FormatRule formatRule) {
		this.formatRule = formatRule;
	}
	
}
