package com.lckp.jproxy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 有效状态
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-06
 */
@Getter
@AllArgsConstructor
public enum ValidStatus {
	VALID(1, "有效"),

	INVALID(0, "无效");

	private final Integer code;

	private final String description;

	/**
	 * 
	 * 根据 code 获取对应的 ValidStatus
	 *
	 * @param code
	 * @return ValidStatus
	 */
	public static ValidStatus getByCode(Integer code) {
		for (ValidStatus validStatus : ValidStatus.values()) {
			if (validStatus.code.equals(code)) {
				return validStatus;
			}
		}
		return INVALID;
	}

	/**
	 * 
	 * 根据 boolean 值获取 code
	 *
	 * @param flag
	 * @return Integer
	 */
	public static Integer getCode(boolean flag) {
		if (flag) {
			return VALID.getCode();
		}
		return INVALID.getCode();
	}
}
