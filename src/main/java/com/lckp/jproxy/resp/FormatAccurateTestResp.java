/**
 * @Title: FormatAccurateTestResp.java
 * @version V1.0
 */
package com.lckp.jproxy.resp;

import com.lckp.jproxy.constant.SeriesType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: FormatAccurateTestResp
 * @description: 精确匹配测试返参
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
@ApiModel("格式化测试返参")
public class FormatAccurateTestResp {
	@ApiModelProperty("格式化结果1")
	private String formatResult1;
	
	@ApiModelProperty("格式化结果2")
	private String formatResult2;
	
	@ApiModelProperty("格式化结果3")
	private String formatResult3;
	
	@ApiModelProperty("seriesType")
	private SeriesType seriesType;
	
	@ApiModelProperty("match")
	private String match;
	
	@ApiModelProperty("replace")
	private String replace;

	/**
	 * @return the formatResult1
	 */
	public String getFormatResult1() {
		return formatResult1;
	}

	/**
	 * @param formatResult1 the formatResult1 to set
	 */
	public void setFormatResult1(String formatResult1) {
		this.formatResult1 = formatResult1;
	}

	/**
	 * @return the formatResult2
	 */
	public String getFormatResult2() {
		return formatResult2;
	}

	/**
	 * @param formatResult2 the formatResult2 to set
	 */
	public void setFormatResult2(String formatResult2) {
		this.formatResult2 = formatResult2;
	}

	/**
	 * @return the formatResult3
	 */
	public String getFormatResult3() {
		return formatResult3;
	}

	/**
	 * @param formatResult3 the formatResult3 to set
	 */
	public void setFormatResult3(String formatResult3) {
		this.formatResult3 = formatResult3;
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
}