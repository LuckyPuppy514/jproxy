/**
 * @Title: AdminUserChangePasswordParam.java
 * @version V1.0
 */
package com.lckp.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: AdminUserChangePasswordParam
 * @description: 后台用户变更密码入参
 * @date 2022年7月18日
 * @author LuckyPuppy514
 */
@ApiModel("后台用户变更密码入参")
public class AdminUserChangePasswordParam {
	@ApiModelProperty(value = "用户名", required = true, example = "jproxy", hidden = true)
	private String username;
	
	@ApiModelProperty(value = "旧密码", required = true, example = "jproxy@2022")
	private String oldPassword;

	@ApiModelProperty(value = "新密码", required = true, example = "jproxy@2022")
	private String newPassword;

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
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 * @throws Exception 
	 */
	public void setNewPassword(String newPassword) throws Exception {
		this.newPassword = newPassword;
	}
}
