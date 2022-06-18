/**
 * @Title: FormatAddRegular.java
 * @version V1.0
 */
package com.lckp.jproxy.param;

import com.lckp.jproxy.constant.SeriesType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: FormatAddRegular
 * @description: 添加规则入参
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
@ApiModel("添加规则入参")
public class FormatAddRegular {
	@ApiModelProperty(required = true, value = "匹配正则")	
	private String match;
	
	@ApiModelProperty(required = true, value = "替换正则")	
	private String replace;

	@ApiModelProperty(required = true, value = "系列类型（动漫：anime，电视剧：serial）", example = "ANIME")	
	private SeriesType seriesType;
	
	/**
	 * @return the match
	 */
	public String getMatch() {
		return match;
	}

	/**
	 * @param match the match to set
	 */
	public void setMatch(String match) {
		this.match = match;
	}

	/**
	 * @return the replace
	 */
	public String getReplace() {
		return replace;
	}

	/**
	 * @param replace the replace to set
	 */
	public void setReplace(String replace) {
		this.replace = replace;
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
}
