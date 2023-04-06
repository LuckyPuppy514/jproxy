package com.lckp.jproxy.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * SonarrTitle
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
@Getter
@Setter
@TableName("sonarr_title")
@Schema(name = "SonarrTitle", description = "SonarrTitle")
public class SonarrTitle implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId
	private Integer id;

	private Integer tvdbId;

	private Integer sno;

	private String mainTitle;

	private String title;

	private String cleanTitle;

	private Integer seasonNumber;

	private Integer monitored;

	private Integer validStatus;

	@TableField(fill = FieldFill.INSERT)
	private String createTime;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateTime;
}