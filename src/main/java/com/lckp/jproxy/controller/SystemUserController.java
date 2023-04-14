package com.lckp.jproxy.controller;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lckp.jproxy.constant.Messages;
import com.lckp.jproxy.entity.SystemUser;
import com.lckp.jproxy.model.request.SystemUserLoginRequest;
import com.lckp.jproxy.service.ISystemUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-12
 */
@Tag(name = "系统用户")
@RequestMapping("/api/system/user")
@RestController
@RequiredArgsConstructor
public class SystemUserController {

	private final ISystemUserService systemUserService;

	private final MessageSource messageSource;

	private int loginWrongCount = 0;

	@Operation(summary = "登陆")
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody @Validated SystemUserLoginRequest request,
			Locale locale) {
		if (loginWrongCount > 10) {
			return ResponseEntity.badRequest()
					.body(messageSource.getMessage(Messages.LOGIN_WRONG_TOO_MANY_TIMES, null, locale));
		}
		SystemUser systemUser = new SystemUser();
		systemUser.setUsername(request.getUsername());
		systemUser.setPassword(request.getPassword());
		if (systemUserService.check(systemUser)) {
			return ResponseEntity.ok(systemUserService.sign(systemUser));
		}
		loginWrongCount++;
		return ResponseEntity.badRequest()
				.body(messageSource.getMessage(Messages.LOGIN_WRONG_USER, null, locale));
	}

	@Operation(summary = "信息")
	@GetMapping("/info")
	public ResponseEntity<SystemUser> info(HttpServletRequest servletRequest) {
		String token = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
		SystemUser systemUser = systemUserService.getSystemUser(token);
		systemUser.setPassword("******");
		return ResponseEntity.ok(systemUser);
	}

	@Operation(summary = "更新")
	@PostMapping("/update")
	public ResponseEntity<Void> update(@RequestBody SystemUser systemUser,
			HttpServletRequest servletRequest) {
		String token = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
		SystemUser currentSystemUser = systemUserService.getSystemUser(token);
		if (StringUtils.isNotBlank(systemUser.getUsername())) {
			currentSystemUser.setUsername(systemUser.getUsername());
		}
		if (StringUtils.isNotBlank(systemUser.getPassword())) {
			currentSystemUser.setPassword(systemUser.getPassword());
		}
		systemUserService.update(currentSystemUser);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "注销")
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest servletRequest) {
		systemUserService.logout(servletRequest.getHeader(HttpHeaders.AUTHORIZATION));
		return ResponseEntity.ok().build();
	}
}
