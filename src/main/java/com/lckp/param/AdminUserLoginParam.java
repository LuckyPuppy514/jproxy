/**
 * @Title: AdminUserLoginParam.java
 * @version V1.0
 */
package com.lckp.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: AdminUserLoginParam
 * @description: 后台用户登录入参
 * @date 2022年7月18日
 * @author LuckyPuppy514
 */
@ApiModel("后台用户登录入参")
public class AdminUserLoginParam {
	@ApiModelProperty(value = "用户名", required = true, example = "jproxy")
	private String username;
	
	@ApiModelProperty(value = "密码", required = true, example = "jproxy@2022")
	private String password;

	@ApiModelProperty(value = "验证码", required = true, example = "123456")
	private String captcha;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the captcha
	 */
	public String getCaptcha() {
		return captcha;
	}

	/**
	 * @param captcha the captcha to set
	 */
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
