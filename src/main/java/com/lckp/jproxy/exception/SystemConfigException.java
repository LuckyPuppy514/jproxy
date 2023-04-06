package com.lckp.jproxy.exception;

/**
 * <p>
 * 系统配置异常
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-31
 */
public class SystemConfigException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SystemConfigException(String message) {
		super(message);
	}
}
