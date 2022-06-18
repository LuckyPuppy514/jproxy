/**
 * @Title: FormatAccurateTestParam.java
 * @version V1.0
 */
package com.lckp.jproxy.param;

import com.lckp.jproxy.constant.SeriesType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: FormatAccurateTestParam
 * @description:精确匹配测试入参
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
@ApiModel(value = "精确匹配测试入参")
public class FormatAccurateTestParam {
	@ApiModelProperty(required = true, value = "测试用例1", example = "[OPFans枫雪动漫][ONE PIECE 海贼王][第1021话][720p][周日版][MP4][简体]")
	private String example1;
	
	@ApiModelProperty(required = false, value = "测试用例2", example = "[OPFans楓雪動漫][ONE PIECE 海賊王][第995話][1080p][周日版][MKV]")
	private String example2;
	
	@ApiModelProperty(required = false, value = "测试用例3", example = "")
	private String example3;
	
	@ApiModelProperty(required = false, value = "匹配正则", example = "\\[Skymoon-Raws\\]\\[(.*) ([^ ]*)\\]\\[(\\d+)](.*)")
	private String match;
	
	@ApiModelProperty(required = false, value = "替换正则", example = "[Skymoon-Raws] $2 / $1 $3 $4")
	private String replace;
	
	@ApiModelProperty(required = true, value = "系列类型（动漫：anime，电视剧：serial）", example = "ANIME")
	private SeriesType seriesType;

	/**
	 * @return the example1
	 */
	public String getExample1() {
		return example1;
	}

	/**
	 * @param example1 the example1 to set
	 */
	public void setExample1(String example1) {
		this.example1 = example1;
	}

	/**
	 * @return the example2
	 */
	public String getExample2() {
		return example2;
	}

	/**
	 * @param example2 the example2 to set
	 */
	public void setExample2(String example2) {
		this.example2 = example2;
	}

	/**
	 * @return the example3
	 */
	public String getExample3() {
		return example3;
	}

	/**
	 * @param example3 the example3 to set
	 */
	public void setExample3(String example3) {
		this.example3 = example3;
	}

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