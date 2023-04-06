package com.lckp.jproxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lckp.jproxy.entity.SystemUser;

/**
 * <p>
 * SystemUser 服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-24
 */
public interface ISystemUserService extends IService<SystemUser> {
	/**
	 * 
	 * 验证账号密码
	 *
	 * @param systemUser
	 * @return boolean
	 */
	public boolean check(SystemUser systemUser);

	/**
	 * 
	 * 签发 token
	 *
	 * @param systemUser
	 * @return String
	 */
	public String sign(SystemUser systemUser);

	/**
	 * 
	 * 校验 token
	 *
	 * @param token
	 * @return boolean
	 */
	public boolean verify(String token);

	/**
	 * 
	 * 注销
	 *
	 * @param token boolean
	 */
	public boolean logout(String token);

	/**
	 * 
	 * 通过 token 获取用户信息
	 *
	 * @param token
	 * @return SystemUser
	 */
	public SystemUser getSystemUser(String token);

	/**
	 * 
	 * 更新用户
	 *
	 * @param systemUser
	 * @return boolean
	 */
	public boolean update(SystemUser systemUser);
}
