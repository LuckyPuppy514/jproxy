package com.lckp.jproxy.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TmdbTitle
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
@Getter
@Setter
@TableName("tmdb_title")
@Schema(name = "TmdbTitle", description = "TmdbTitle")
public class TmdbTitle implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	private Integer tvdbId;

	private Integer tmdbId;

	private String language;

	private String title;

	@TableField(exist = false)
	private String cleanTitle;

	private Integer validStatus;

	@TableField(fill = FieldFill.INSERT)
	private String createTime;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateTime;
}