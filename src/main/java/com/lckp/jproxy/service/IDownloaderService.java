package com.lckp.jproxy.service;

import java.util.List;

/**
 * <p>
 * 下载器服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
public interface IDownloaderService {
	/**
	 * 
	 * 登录
	 *
	 * @param url
	 * @param username
	 * @param password
	 * @return boolean
	 */
	public boolean login(String url, String username, String password);

	/**
	 * 
	 * 登录
	 *
	 * @return boolean
	 */
	public boolean login();

	/**
	 * 
	 * 获取登录状态
	 *
	 * @return boolean
	 */
	public boolean isLogin();

	/**
	 * 
	 * 获取文件列表
	 *
	 * @param hash
	 * @return List<String>
	 */
	public List<String> files(String hash);

	/**
	 * 
	 * 重命名
	 *
	 * @param hash
	 * @param name
	 * @return boolean
	 */
	public boolean rename(String hash, String name);

	/**
	 * 
	 * 重命名文件
	 *
	 * @param hash
	 * @param oldPath
	 * @param newPath
	 * @return boolean
	 */
	public boolean renameFile(String hash, String oldPath, String newPath);
}
