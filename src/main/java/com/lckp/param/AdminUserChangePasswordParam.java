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
	@ApiModelProperty(value = "新用户名", required = true, example = "jproxy")
	private String newUsername;
	
	@ApiModelProperty(value = "旧用户名", required = true, example = "jproxy", hidden = true)
	private String oldUsername;
	
	@ApiModelProperty(value = "旧密码", required = true, example = "jproxy@2022")
	private String oldPassword;

	@ApiModelProperty(value = "新密码", required = true, example = "jproxy@2022")
	private String newPassword;

	/**
	 * @return the newUsername
	 */
	public String getNewUsername() {
		return newUsername;
	}

	/**
	 * @param newUsername the newUsername to set
	 */
	public void setNewUsername(String newUsername) {
		this.newUsername = newUsername;
	}

	/**
	 * @return the oldUsername
	 */
	public String getOldUsername() {
		return oldUsername;
	}

	/**
	 * @param oldUsername the oldUsername to set
	 */
	public void setOldUsername(String oldUsername) {
		this.oldUsername = oldUsername;
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
