package com.lckp.jproxy.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 格式化结果
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-04-07
 */
@Getter
@Setter
public class FormatResult implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 * 格式化结果
	 */
	private String formatText;

	/*
	 * 剩余信息
	 */
	private String restText;
}
