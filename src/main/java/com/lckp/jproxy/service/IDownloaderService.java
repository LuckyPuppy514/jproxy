package com.lckp.jproxy.service;

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
	 * byte[] content
	 *
	 * @param xml
	 * @return String
	 */
	public byte[] executeFormatRule(byte[] content);
}
