/**
 * @Title: ApiConfig.java
 * @version V1.0
 */
package com.lckp.jproxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.lckp.jproxy.model.Format;

/**
 * @className: ApiConfig
 * @description: api配置
 * @date 2022年6月11日
 * @author LuckyPuppy514
 */
@Component
@ConfigurationProperties(prefix = "api")
public class ApiConfig {
	/*
	 * jackett url
	 */
	private String jackett;
	
	/*
	 * prowlarr url
	 */
	private String prowlarr;
	
	/*
	 * 格式化配置
	 */
	private Format format;

	/**
	 * @return the jackett
	 */
	public String getJackett() {
		return jackett;
	}

	/**
	 * @param jackett the jackett to set
	 */
	public void setJackett(String jackett) {
		this.jackett = jackett;
	}

	/**
	 * @return the prowlarr
	 */
	public String getProwlarr() {
		return prowlarr;
	}

	/**
	 * @param prowlarr the prowlarr to set
	 */
	public void setProwlarr(String prowlarr) {
		this.prowlarr = prowlarr;
	}

	/**
	 * @return the format
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(Format format) {
		this.format = format;
	}

}
