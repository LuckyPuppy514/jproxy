package com.lckp.jproxy.controller;

import com.lckp.jproxy.constant.Messages;
import com.lckp.jproxy.entity.SystemUser;
import com.lckp.jproxy.model.request.SystemUserLoginRequest;
import com.lckp.jproxy.service.ISystemUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

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

    @Value("${login-enabled}")
    private boolean loginEnabled;

    @Operation(summary = "登陆")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Validated SystemUserLoginRequest request,
                                        Locale locale) {
        // 登录已关闭，自动登录匿名账号
        if (!loginEnabled) {
            SystemUser systemUser = new SystemUser();
            systemUser.setUsername("Anonymous");
            systemUser.setPassword("Anonymous");
            return ResponseEntity.ok(systemUserService.sign(systemUser));
        }

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
    public ResponseEntity<String> update(@RequestBody SystemUser systemUser,
                                       HttpServletRequest servletRequest, Locale locale) {
        // 登录已关闭，禁止更新用户信息
        if (!loginEnabled) {
            return new ResponseEntity<>(messageSource.getMessage(Messages.LOGIN_WRONG_USER, null, locale), HttpStatus.UNAUTHORIZED);
        }

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

    @Operation(summary = "登录开启状态")
    @GetMapping("/isLoginEnabled")
    public ResponseEntity<Boolean> isLoginEnabled() {
        return ResponseEntity.ok(loginEnabled);
    }
}
