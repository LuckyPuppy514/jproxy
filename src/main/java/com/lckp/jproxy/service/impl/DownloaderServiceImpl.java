package com.lckp.jproxy.service.impl;

import org.springframework.stereotype.Service;

import com.lckp.jproxy.service.IDownloaderService;

/**
 * <p>
 * 下载器服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Service
public class DownloaderServiceImpl implements IDownloaderService {
	
	/**
	 * @param content
	 * @return
	 * @see com.lckp.jproxy.service.IDownloaderService#executeFormatRule(byte[])
	 */
	@Override
	public byte[] executeFormatRule(byte[] content) {
		return content;
	}

}
