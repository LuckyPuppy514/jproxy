/**
 * @Title: AdminUser.java
 * @version V1.0
 */
package com.lckp.model;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @className: AdminUser
 * @description: 后台用户
 * @date 2022年7月18日
 * @author LuckyPuppy514
 */
@ApiModel("后台用户")
public class AdminUser {
	@ApiModelProperty(value = "后台用户 ID（主键）")
	private Integer userId;
	
	@ApiModelProperty(value = "用户名")
	@NotBlank(message = "用户名不能为空")
	private String username;
	
	@ApiModelProperty(value = "密码")
	@NotBlank(message = "密码不能为空")
	private String password;
	
	@ApiModelProperty(value = "有效状态（有效：1，无效：0，默认：1）")
	private Integer validstatus;
	
	@ApiModelProperty(value = "创建时间")
	private String createTime;
	
	@ApiModelProperty(value = "更新时间")
	private String updateTime;

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

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
	 * @return the validstatus
	 */
	public Integer getValidstatus() {
		return validstatus;
	}

	/**
	 * @param validstatus the validstatus to set
	 */
	public void setValidstatus(Integer validstatus) {
		this.validstatus = validstatus;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}