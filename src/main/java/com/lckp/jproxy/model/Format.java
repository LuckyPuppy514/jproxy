/**
 * @Title: Format.java
 * @version V1.0
 */
package com.lckp.jproxy.model;

/**
 * @className: Format
 * @description: 格式化配置
 * @date 2022年6月10日
 * @author LuckyPuppy514
 */
public class Format {
	/*
	 * 是否格式化（默认：true）
	 */
	private boolean enable;
	
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

	/*
	 * 动漫（默认类型）
	 */
	private Rule anime;
	
	/*
	 * 电视剧
	 */
	private Rule serial;

	/**
	 * @return the anime
	 */
	public Rule getAnime() {
		return anime;
	}

	/**
	 * @param anime the anime to set
	 */
	public void setAnime(Rule anime) {
		this.anime = anime;
	}

	/**
	 * @return the serial
	 */
	public Rule getSerial() {
		return serial;
	}

	/**
	 * @param serial the serial to set
	 */
	public void setSerial(Rule serial) {
		this.serial = serial;
	}
	
}
