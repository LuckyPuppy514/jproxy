/**
 * @Title: FormatConfig.java
 * @version V1.0
 */
package com.lckp.jproxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.lckp.jproxy.model.FormatType;

/**
 * @className: FormatConfig
 * @description: 格式化配置
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
@Component
@ConfigurationProperties(prefix = "format")
public class FormatConfig {
	// 动漫
	private FormatType official;
	
	// 电视剧
	private FormatType custom;
	
	// 开关
	private boolean enable;

	/**
	 * @return the official
	 */
	public FormatType getOfficial() {
		return official;
	}

	/**
	 * @param official the official to set
	 */
	public void setOfficial(FormatType official) {
		this.official = official;
	}

	/**
	 * @return the custom
	 */
	public FormatType getCustom() {
		return custom;
	}

	/**
	 * @param custom the custom to set
	 */
	public void setCustom(FormatType custom) {
		this.custom = custom;
	}

	/**
	 * @return the enable
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * @param enable the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
