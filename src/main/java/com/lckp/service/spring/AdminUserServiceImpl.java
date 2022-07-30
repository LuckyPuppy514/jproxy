package com.lckp.service.spring;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lckp.mapper.IAdminUserMapper;
import com.lckp.param.AdminUserChangePasswordParam;
import com.lckp.param.AdminUserLoginParam;
import com.lckp.service.facade.IAdminUserService;

/**
* @ClassName: AdminUserServiceImpl
* @Description: 后台用户serviceImpl
* @author LuckyPuppy514
* @date 2022-07-18 15:24:37
*
*/
@Service("adminUserService")
public class AdminUserServiceImpl implements IAdminUserService {
	
	@Autowired
	private IAdminUserMapper adminUserMapper;

	/**
	 * 登录
	 */
	@Override
	public int login(AdminUserLoginParam param) {
		return adminUserMapper.login(param);
	}

	/**
	 * 变更密码
	 */
	@Override
	public int changePassword(AdminUserChangePasswordParam param) {
		return adminUserMapper.changePassword(param);
	}

}
