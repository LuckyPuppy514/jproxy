package com.lckp.jproxy.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * RadarrRule
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Getter
@Setter
@TableName("radarr_rule")
@Schema(name = "RadarrRule", description = "RadarrRule")
public class RadarrRule implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	@NotBlank
	private String token;

	@NotNull
	private Integer priority;

	@NotBlank
	private String regex;

	private String replacement;

	@NotNull
	private Integer offset;

	@NotBlank
	private String example;

	@NotBlank
	private String remark;

	@NotBlank
	private String author;

	private Integer validStatus;

	@TableField(fill = FieldFill.INSERT)
	private String createTime;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateTime;
}