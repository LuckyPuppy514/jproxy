package com.lckp.jproxy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 下载器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-04-13
 */
@Getter
@AllArgsConstructor
public enum Downloader {

	QBITTORRENT("qBittorrent"),

	TRANSMISSION("Transmission");

	private final String name;
}
