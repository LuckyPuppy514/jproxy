/**
 * @Title: JackettConfig.java
 * @version V1.0
 */
package com.lckp.jproxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.lckp.jproxy.model.Format;

/**
 * @className: JackettConfig
 * @description:  jackett配置类
 * @date 2022年6月10日
 * @author LuckyPuppy514
 */
@Component
@ConfigurationProperties(prefix = "jackett")
public class JackettConfig {
	/*
	 * jackett url
	 */
	private String url;
	
	/*
	 * 格式化配置
	 */
	private Format format;

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
