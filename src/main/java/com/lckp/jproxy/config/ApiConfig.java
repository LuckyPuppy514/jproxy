/**
 * @Title: ApiConfig.java
 * @version V1.0
 */
package com.lckp.jproxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
}
