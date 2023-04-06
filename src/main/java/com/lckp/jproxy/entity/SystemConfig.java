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
 * SystemConfig
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
@Getter
@Setter
@TableName("system_config")
@Schema(name = "SystemConfig", description = "SystemConfig")
public class SystemConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId
	private Integer id;

	private String key;

	private String value;

	private Integer validStatus;

	@TableField(fill = FieldFill.INSERT)
	private String createTime;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateTime;
}