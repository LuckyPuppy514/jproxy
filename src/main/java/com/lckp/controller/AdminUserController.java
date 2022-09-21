package com.lckp.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lckp.constant.Field;
import com.lckp.constant.Message;
import com.lckp.interceptor.LoginInterceptor;
import com.lckp.param.AdminUserChangePasswordParam;
import com.lckp.param.AdminUserLoginParam;
import com.lckp.resp.ResVo;
import com.lckp.service.facade.IAdminUserService;
import com.lckp.util.CaptchaUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;


/**
* @ClassName: AdminUserController
* @Description: 后台用户Controller
* @author LuckyPuppy514
* @date 2022-07-18 15:24:37
*
*/
@RestController
@RequestMapping("/adminUser")
@Api(tags = "后台用户")
public class AdminUserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserController.class);
	
	@Autowired
	private IAdminUserService adminUserService;
	@Autowired
	private MessageSource messageSource;
	
	@ApiOperation("验证码")
	@GetMapping("/captcha")
	public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CaptchaUtil.out(request, response);
	}
	
	@ApiOperation("登录")
	@PostMapping("/login")
	public ResVo<String> login(AdminUserLoginParam param,@ApiIgnore Locale locale, HttpServletRequest request){
		LOGGER.debug("后台用户 - 登录：{},{}", JSON.toJSONString(param));
		if (!CaptchaUtil.ver(param.getCaptcha(), request)) {
			return ResVo.fail(Message.CAPTCHA_IS_WRONG, messageSource, locale);
		}
		
		if (adminUserService.login(param) > 0) {
			LoginInterceptor.login(request, param.getUsername());
			CaptchaUtil.clear(request);
			return ResVo.success();
		}
		return ResVo.fail(Message.USERNAME_OR_PASSWORD_IS_WRONG, messageSource, locale);
	}
	
	@ApiOperation("注销")
	@PostMapping("/logout")
	public ResVo<String> logout(@ApiIgnore Locale locale, HttpServletRequest request){
		LOGGER.debug("后台用户 - 注销");
		LoginInterceptor.logout(request);
		return ResVo.success();
	}
	
	@ApiOperation("变更密码")
	@PostMapping("/changePassword")
	public ResVo<String> changePassword(AdminUserChangePasswordParam param,@ApiIgnore Locale locale, HttpServletRequest request) throws Exception{
		param.setOldUsername((String)request.getSession().getAttribute(Field.USERNAME));
		LOGGER.debug("后台用户 - 变更密码：{}", JSON.toJSONString(param));
		if (adminUserService.changePassword(param) > 0) {
			return ResVo.success();
		}
		return ResVo.fail(Message.USERNAME_OR_PASSWORD_IS_WRONG, messageSource, locale);
	}
}
