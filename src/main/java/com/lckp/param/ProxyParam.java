/**
 * @Title: ProxyParam.java
 * @version V1.0
 */
package com.lckp.param;

import org.apache.commons.lang3.StringUtils;

import com.lckp.config.JProxyConfiguration;
import com.lckp.constant.Field;
import com.lckp.constant.ProxyType;
import com.lckp.constant.SearchType;
import com.lckp.util.FormatUtil;

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
	
	@ApiModelProperty("查询关键字")
	private String searchKey;
	
	@ApiModelProperty("查询类型")
	private String searchType;
	
	@ApiModelProperty("季")
	private String season;
	
	@ApiModelProperty("集")
	private String ep;
	
	@ApiModelProperty("查询参数")
	private String paramString;
	
	private static final String DATE_REGEX = "\\d+\\.\\d+\\.\\d+";
	private static final String SEASON_AND_EP_REGEX = "S\\d+E\\d+";

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
	 * @return the searchKey
	 */
	public String getSearchKey() {
		return searchKey;
	}

	/**
	 * @param searchKey the searchKey to set
	 */
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	/**
	 * @return the searchType
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * @return the season
	 */
	public String getSeason() {
		return season;
	}

	/**
	 * @param season the season to set
	 */
	public void setSeason(String season) {
		if(StringUtils.isNotBlank(season) && season.length() == 1) {
			season = "0" + season;
		}
		this.season = season;
	}

	/**
	 * @return the ep
	 */
	public String getEp() {
		return ep;
	}

	/**
	 * @param ep the ep to set
	 */
	public void setEp(String ep) {
		if(StringUtils.isNotBlank(ep) && ep.length() == 1) {
			ep = "0" + ep;
		}
		this.ep = ep;
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
		boolean flag = false;
		StringBuffer paramStringBuffer = new StringBuffer(paramString);
		if(StringUtils.isNotBlank(this.searchKey)) {
			StringBuffer buffer = new StringBuffer(this.searchKey);
			// 合并季集（Standard）/日期（Daily）到查询关键字字段
			if (SearchType.tvsearch.toString().equals(this.searchType)) {
				if (StringUtils.isNotBlank(this.season) && StringUtils.isNotBlank(this.ep)) {
					buffer.append(" ");
					if(this.season.trim().length() == 4) {
						buffer.append(this.season + "." + this.ep.replace("/", "."));
					} else {
						buffer.append("S" + this.season + "E" + this.ep);
					}
					flag = true;
				}
			}
			// 格式化查询关键字
			String sk = FormatUtil.search(buffer.toString(), JProxyConfiguration.searchRuleList);
			this.searchKey = sk;
			
			// 分离季集/日期
			if(flag) {
				int index = sk.lastIndexOf(" ");
				if (index != -1) {
					String seasonAndEpOrDate = sk.substring(index + 1);
					if(seasonAndEpOrDate.matches(SEASON_AND_EP_REGEX)) {
						sk = sk.substring(0, index);
						index = seasonAndEpOrDate.indexOf("E");
						this.season = seasonAndEpOrDate.substring(1, index);
						this.ep = seasonAndEpOrDate.substring(index + 1);
						paramStringBuffer.append("&" + Field.SEASON + "=" + this.season);
						paramStringBuffer.append("&" + Field.EP + "=" + this.ep);
						
					} else if(seasonAndEpOrDate.matches(DATE_REGEX)) {
						sk = sk.substring(0, index);
						index = seasonAndEpOrDate.indexOf(".");
						this.season = seasonAndEpOrDate.substring(0, index);
						this.ep = seasonAndEpOrDate.substring(index + 1).replace(".", "/");
						paramStringBuffer.append("&" + Field.SEASON + "=" + this.season);
						paramStringBuffer.append("&" + Field.EP + "=" + this.ep);
					}
				}
			}
			
			paramStringBuffer.append("&" + Field.SEARCH_KEY + "=" + sk);
		}

		this.paramString = paramStringBuffer.toString();
	}
}