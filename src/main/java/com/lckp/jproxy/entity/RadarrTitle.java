package com.lckp.jproxy.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * RadarrTitle
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Getter
@Setter
@TableName("radarr_title")
@Schema(name = "RadarrTitle", description = "RadarrTitle")
public class RadarrTitle implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer tmdbId;

	private Integer sno;

	private String mainTitle;

	private String title;

	private String cleanTitle;

	private Integer year;

	private Integer monitored;

	private Integer validStatus;

	@TableField(fill = FieldFill.INSERT)
	private String createTime;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateTime;
}