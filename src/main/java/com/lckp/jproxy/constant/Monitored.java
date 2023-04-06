package com.lckp.jproxy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 监控状态
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-06
 */
@Getter
@AllArgsConstructor
public enum Monitored {

	CONTINUING(0, false, "未监控"),

	ENDED(1, true, "监控中");

	private final Integer code;

	private final boolean flag;

	private final String description;

	/**
	 * 
	 * 根据 flag 获取对应的 Monitored
	 *
	 * @param flag
	 * @return Monitored
	 */
	public static Monitored getByFlag(boolean flag) {
		if (flag) {
			return ENDED;
		}
		return CONTINUING;
	}
}