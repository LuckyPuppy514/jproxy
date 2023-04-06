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
 * RadarrExample
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-30
 */
@Getter
@Setter
@TableName("radarr_example")
@Schema(name = "RadarrExample", description = "RadarrExample")
public class RadarrExample implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId
	private String hash;

	private String originalText;

	private String formatText;

	private Integer validStatus;

	@TableField(fill = FieldFill.INSERT)
	private String createTime;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateTime;
}