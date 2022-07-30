package com.lckp.service.facade;

import com.lckp.param.AdminUserChangePasswordParam;
import com.lckp.param.AdminUserLoginParam;

/**
* @ClassName: IAdminUserService
* @Description: 后台用户service
* @author LuckyPuppy514
* @date 2022-07-18 15:24:37
*
*/
public interface IAdminUserService {
	// 登录
	public int login(AdminUserLoginParam param);
	
	// 变更密码
	public int changePassword(AdminUserChangePasswordParam param);
}
