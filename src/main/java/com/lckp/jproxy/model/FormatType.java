/**
 * @Title: FormatType.java
 * @version V1.0
 */
package com.lckp.jproxy.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: FormatType
 * @description: 格式化类型
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
@ApiModel("格式化类型")
public class FormatType {
	@ApiModelProperty("动漫")
	private FormatRule anime;
	
	@ApiModelProperty("电视剧")
	private FormatRule serial;

	/**
	 * @return the anime
	 */
	public FormatRule getAnime() {
		return anime;
	}

	/**
	 * @param anime the anime to set
	 */
	public void setAnime(FormatRule anime) {
		this.anime = anime;
	}

	/**
	 * @return the serial
	 */
	public FormatRule getSerial() {
		return serial;
	}

	/**
	 * @param serial the serial to set
	 */
	public void setSerial(FormatRule serial) {
		this.serial = serial;
	}
}
