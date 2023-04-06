package com.lckp.jproxy.model.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统用户登陆入参
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-24
 */
@Getter
@Setter
@Schema(description = "系统用户登陆入参")
public class SystemUserLoginRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "用户名", example = "admin")
	@NotBlank
	private String username;

	@Schema(description = "密码", example = "12345678")
	@NotBlank
	private String password;
}
